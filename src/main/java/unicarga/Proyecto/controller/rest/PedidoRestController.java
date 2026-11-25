package unicarga.Proyecto.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicarga.Proyecto.calculo.ResultadoCalculo;
import unicarga.Proyecto.enums.Estado;
import unicarga.Proyecto.exceptions.ResourcesNotFoundException;
import unicarga.Proyecto.model.Contiene;
import unicarga.Proyecto.model.Pedido;
import unicarga.Proyecto.model.Recibe;
import unicarga.Proyecto.model.Transporta;
import unicarga.Proyecto.service.CalculoService;
import unicarga.Proyecto.service.ContieneService;
import unicarga.Proyecto.service.PedidoService;
import unicarga.Proyecto.service.RecibeService;
import unicarga.Proyecto.service.TransportaService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoRestController {

    private final PedidoService pedidoService;
    private final ContieneService contieneService;
    private final RecibeService recibeService;
    private final TransportaService transportaService;
    private final CalculoService calculoService;

    /** GET /api/pedidos            -> todos
     *  GET /api/pedidos?estado=PENDIENTE | FINALIZADO */
    @GetMapping
    public List<Pedido> listar(@RequestParam(required = false) Estado estado) {
        return estado == null ? pedidoService.findAll() : pedidoService.findByEstado(estado);
    }

    @GetMapping("/{id}")
    public Pedido obtener(@PathVariable Long id) {
        return pedidoService.findById(id);
    }

    /** Productos (Contiene) del pedido */
    @GetMapping("/{id}/detalles")
    public List<Contiene> detalles(@PathVariable Long id) {
        pedidoService.findById(id);
        return contieneService.findByPedidoId(id);
    }

    /** Cliente (Recibe) del pedido */
    @GetMapping("/{id}/cliente")
    public Recibe cliente(@PathVariable Long id) {
        return recibeService.findByPedidoId(id)
                .orElseThrow(() -> new ResourcesNotFoundException("El pedido no tiene cliente asignado"));
    }

    /** Unidades de transporte asignadas al pedido (resultado del cálculo), con su cantidad. */
    @GetMapping("/{id}/transportes")
    public List<Transporta> transportes(@PathVariable Long id) {
        pedidoService.findById(id);
        return transportaService.findByPedidoId(id);
    }

    /**
     * Sugiere combinaciones de unidades para el pedido SIN guardar nada.
     * La primera combinación es la recomendada.
     */
    @GetMapping("/{id}/calculo")
    public ResultadoCalculo sugerir(@PathVariable Long id) {
        return calculoService.sugerir(id);
    }

    /**
     * Calcula y guarda la combinación elegida (opcion=1 es la recomendada).
     * 200 con el resultado; 422 si no existe ninguna combinación válida (ver "motivo").
     */
    @PostMapping("/{id}/calculo")
    public ResponseEntity<ResultadoCalculo> calcular(@PathVariable Long id,
                                                     @RequestParam(defaultValue = "1") int opcion) {
        ResultadoCalculo resultado = calculoService.calcularYAplicar(id, opcion);
        return resultado.viable()
                ? ResponseEntity.ok(resultado)
                : ResponseEntity.unprocessableEntity().body(resultado);
    }

    /**
     * Cuerpo esperado: usuario {idUsuario} obligatorio. Las unidades de transporte, espacios y peso
     * se asignan con POST /api/pedidos/{id}/calculo.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido crear(@RequestBody Pedido pedido) {
        pedido.setIdPedido(null);
        if (pedido.getEstado() == null) pedido.setEstado(Estado.PENDIENTE);
        if (pedido.getFechaPedido() == null) pedido.setFechaPedido(LocalDateTime.now());
        if (pedido.getEspacioUsado() == null) pedido.setEspacioUsado(BigDecimal.ZERO);
        if (pedido.getEspacioLibre() == null) pedido.setEspacioLibre(BigDecimal.ZERO);
        if (pedido.getPesoTotal() == null) pedido.setPesoTotal(BigDecimal.ZERO);
        return pedidoService.save(pedido);
    }

    @PutMapping("/{id}")
    public Pedido actualizar(@PathVariable Long id, @RequestBody Pedido pedido) {
        Pedido actual = pedidoService.findById(id);
        pedido.setIdPedido(id);
        // Los campos que el cliente no envíe conservan su valor actual.
        if (pedido.getEspacioUsado() == null) pedido.setEspacioUsado(actual.getEspacioUsado());
        if (pedido.getEspacioLibre() == null) pedido.setEspacioLibre(actual.getEspacioLibre());
        if (pedido.getPesoTotal() == null) pedido.setPesoTotal(actual.getPesoTotal());
        if (pedido.getEstado() == null) pedido.setEstado(actual.getEstado());
        if (pedido.getFechaPedido() == null) pedido.setFechaPedido(actual.getFechaPedido());
        if (pedido.getUsuario() == null) pedido.setUsuario(actual.getUsuario());
        return pedidoService.save(pedido);
    }

    /** Elimina el pedido junto con sus productos, su cliente y sus unidades asignadas. */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        pedidoService.eliminarPedidoCompleto(id);
    }
}
