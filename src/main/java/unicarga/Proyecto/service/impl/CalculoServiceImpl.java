package unicarga.Proyecto.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unicarga.Proyecto.calculo.CalculadoraTransporte;
import unicarga.Proyecto.calculo.Combinacion;
import unicarga.Proyecto.calculo.ItemCarga;
import unicarga.Proyecto.calculo.ResultadoCalculo;
import unicarga.Proyecto.calculo.TipoUnidad;
import unicarga.Proyecto.exceptions.ResourcesNotFoundException;
import unicarga.Proyecto.model.Pedido;
import unicarga.Proyecto.model.Producto;
import unicarga.Proyecto.model.Transporta;
import unicarga.Proyecto.service.CalculoService;
import unicarga.Proyecto.service.ContieneService;
import unicarga.Proyecto.service.PedidoService;
import unicarga.Proyecto.service.TransportaService;
import unicarga.Proyecto.service.UnidadTransporteService;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculoServiceImpl implements CalculoService {

    private final PedidoService pedidoService;
    private final ContieneService contieneService;
    private final UnidadTransporteService unidadTransporteService;
    private final TransportaService transportaService;

    /** Fracción del volumen físico de cada unidad que se considera utilizable (0.90 = 90 %). */
    @Value("${unicarga.calculo.factor-aprovechamiento:0.90}")
    private BigDecimal factorAprovechamiento;

    /** Máximo de unidades que puede tener una combinación. */
    @Value("${unicarga.calculo.max-unidades:10}")
    private int maxUnidades;

    /** Cuántas combinaciones se sugieren como máximo. */
    @Value("${unicarga.calculo.max-combinaciones:3}")
    private int maxCombinaciones;

    @Override
    @Transactional(readOnly = true)
    public ResultadoCalculo sugerir(Long idPedido) {
        pedidoService.findById(idPedido);   // 404 si no existe

        List<ItemCarga> items = contieneService.findByPedidoId(idPedido).stream()
                .map(c -> {
                    Producto p = c.getProducto();
                    return new ItemCarga(p.getNombreProducto(), p.getLargoProducto(), p.getAnchoProducto(),
                            p.getAltoProducto(), p.getPesoProducto(), c.getTotalProducto());
                })
                .toList();

        List<TipoUnidad> tipos = unidadTransporteService.findAll().stream()
                .map(u -> new TipoUnidad(u.getIdUnidadTransporte(), u.getTipoTransporte(),
                        u.getLargoTransporte(), u.getAnchoTransporte(), u.getAltoTransporte(),
                        u.getPesoTransporte()))
                .toList();

        return new CalculadoraTransporte(factorAprovechamiento, maxUnidades, maxCombinaciones)
                .calcular(items, tipos);
    }

    @Override
    @Transactional
    public ResultadoCalculo calcularYAplicar(Long idPedido, int opcion) {
        Pedido pedido = pedidoService.findById(idPedido);
        ResultadoCalculo resultado = sugerir(idPedido);
        if (!resultado.viable()) {
            return resultado;
        }
        if (opcion < 1 || opcion > resultado.combinaciones().size()) {
            throw new ResourcesNotFoundException("La opción " + opcion + " no existe; hay "
                    + resultado.combinaciones().size() + " combinación(es) disponible(s).");
        }
        Combinacion elegida = resultado.combinaciones().get(opcion - 1);

        transportaService.deleteByPedidoId(idPedido);
        transportaService.saveAll(elegida.lineas().stream()
                .map(l -> Transporta.builder()
                        .pedido(pedido)
                        .unidadTransporte(unidadTransporteService.findById(l.idUnidadTransporte()))
                        .cantidad(l.cantidad())
                        .build())
                .toList());

        pedido.setEspacioUsado(elegida.espacioUsadoM3());
        pedido.setEspacioLibre(elegida.espacioLibreM3());
        pedido.setPesoTotal(resultado.pesoTotalKg());
        pedidoService.save(pedido);
        return resultado;
    }
}
