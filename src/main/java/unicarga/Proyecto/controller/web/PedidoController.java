package unicarga.Proyecto.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import unicarga.Proyecto.calculo.Combinacion;
import unicarga.Proyecto.calculo.LineaCombinacion;
import unicarga.Proyecto.calculo.ResultadoCalculo;
import unicarga.Proyecto.config.AuthInterceptor;
import unicarga.Proyecto.enums.Estado;
import unicarga.Proyecto.model.*;
import unicarga.Proyecto.service.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Módulo "Pedido": lista de pendientes, armado del pedido (+/-), detalles,
 * finalizar y eliminar.
 */
@Controller
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final ProductoService productoService;
    private final ClienteService clienteService;
    private final ContieneService contieneService;
    private final RecibeService recibeService;
    private final TransportaService transportaService;
    private final CalculoService calculoService;

    // ------------------------------------------------------------ LISTA DE PENDIENTES
    @GetMapping
    public String lista(@RequestParam(required = false) Long selectedId, Model model) {
        List<Pedido> pendientes = pedidoService.findByEstado(Estado.PENDIENTE);

        Pedido seleccionado = null;
        if (selectedId != null) {
            seleccionado = pendientes.stream()
                    .filter(p -> p.getIdPedido().equals(selectedId))
                    .findFirst().orElse(null);
        }

        model.addAttribute("pedidosPendientes", pendientes);
        model.addAttribute("pedidoSeleccionado", seleccionado);
        model.addAttribute("clientes", clienteService.findAll());   // combo del modal "Nuevo pedido"
        if (seleccionado != null) {
            model.addAttribute("clienteSeleccionado",
                    recibeService.findByPedidoId(selectedId).map(Recibe::getCliente).orElse(null));
            model.addAttribute("detallesSeleccionado", contieneService.findByPedidoId(selectedId));
        }
        return "pedido_lista";
    }

    // ------------------------------------------------------------ NUEVO PEDIDO
    /** Crea un pedido vacío con la fecha actual, el usuario en sesión y el cliente elegido en el combo. */
    @PostMapping("/nuevo")
    public String nuevo(@RequestParam Long clienteId,
                        @SessionAttribute(AuthInterceptor.SESSION_USUARIO) Usuario usuario,
                        RedirectAttributes ra) {
        Cliente cliente = clienteService.findClienteById(clienteId);

        // Las unidades de transporte se asignan al calcular (tabla Transporta).
        Pedido pedido = pedidoService.save(Pedido.builder()
                .total(0)
                .espacioUsado(BigDecimal.ZERO)
                .espacioLibre(BigDecimal.ZERO)
                .pesoTotal(BigDecimal.ZERO)
                .estado(Estado.PENDIENTE)
                .fechaPedido(LocalDateTime.now())
                .usuario(usuario)
                .build());

        recibeService.save(Recibe.builder().pedido(pedido).cliente(cliente).build());
        return "redirect:/pedidos/editar/" + pedido.getIdPedido();
    }

    // ------------------------------------------------------------ ARMAR PEDIDO
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id,
                         @RequestParam(required = false) Long prodId,
                         Model model) {
        Pedido pedido = pedidoService.findById(id);
        List<Contiene> detalles = contieneService.findByPedidoId(id);

        Producto activo = prodId != null ? productoService.findById(prodId) : null;
        int cantidadActual = activo == null ? 0 : detalles.stream()
                .filter(c -> c.getProducto().getIdProducto().equals(prodId))
                .mapToInt(Contiene::getTotalProducto).sum();

        model.addAttribute("pedido", pedido);
        model.addAttribute("detalles", detalles);
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("productoActivo", activo);
        model.addAttribute("cantidadActual", cantidadActual);
        return "pedido_armar";
    }

    @PostMapping("/{id}/sumar/{prodId}")
    public String sumar(@PathVariable Long id, @PathVariable Long prodId) {
        Pedido pedido = pedidoService.findById(id);
        Producto producto = productoService.findById(prodId);

        Contiene linea = buscarLinea(id, prodId);
        if (linea == null) {
            linea = Contiene.builder().pedido(pedido).producto(producto).totalProducto(0).build();
        }
        linea.setTotalProducto(linea.getTotalProducto() + 1);
        contieneService.save(linea);

        actualizarTotal(pedido);
        return "redirect:/pedidos/editar/" + id + "?prodId=" + prodId;
    }

    @PostMapping("/{id}/restar/{prodId}")
    public String restar(@PathVariable Long id, @PathVariable Long prodId) {
        Pedido pedido = pedidoService.findById(id);

        Contiene linea = buscarLinea(id, prodId);
        if (linea != null) {
            if (linea.getTotalProducto() <= 1) {
                contieneService.delete(linea);
            } else {
                linea.setTotalProducto(linea.getTotalProducto() - 1);
                contieneService.save(linea);
            }
            actualizarTotal(pedido);
        }
        return "redirect:/pedidos/editar/" + id + "?prodId=" + prodId;
    }

    // ------------------------------------------------------------ CALCULAR (PENDIENTE)
    /**
     * Calcula las combinaciones de unidades y guarda la elegida en el pedido.
     * {@code opcion} = 1 es la recomendada; la pantalla de detalles permite elegir otra.
     * Es POST porque modifica el pedido (unidades asignadas y espacios).
     */
    @PostMapping("/{id}/calcular")
    public String calcular(@PathVariable Long id,
                           @RequestParam(defaultValue = "1") int opcion,
                           RedirectAttributes ra) {
        pedidoService.findById(id);
        if (contieneService.findByPedidoId(id).isEmpty()) {
            ra.addFlashAttribute("error", "Agrega al menos un producto antes de calcular.");
            return "redirect:/pedidos/editar/" + id;
        }

        ResultadoCalculo resultado = calculoService.calcularYAplicar(id, opcion);
        if (!resultado.viable()) {
            ra.addFlashAttribute("error", resultado.motivo());
            return "redirect:/pedidos/editar/" + id;
        }
        return "redirect:/pedidos/" + id + "/detalles";
    }

    @GetMapping("/{id}/detalles")
    public String detalles(@PathVariable Long id, Model model) {
        Pedido pedido = pedidoService.findById(id);
        List<Transporta> transportes = transportaService.findByPedidoId(id);

        model.addAttribute("pedido", pedido);
        model.addAttribute("detalles", contieneService.findByPedidoId(id));
        model.addAttribute("transportes", transportes);
        model.addAttribute("porcentajeUsado", PedidoUtil.porcentajeUsado(pedido));

        // Combinaciones alternativas (solo si el pedido ya fue calculado y sigue pendiente).
        if (pedido.getEstado() == Estado.PENDIENTE && !transportes.isEmpty()) {
            ResultadoCalculo sugerencias = calculoService.sugerir(id);
            if (sugerencias.viable()) {
                List<LineaCombinacion> actuales = transportes.stream()
                        .map(t -> new LineaCombinacion(t.getUnidadTransporte().getIdUnidadTransporte(),
                                t.getUnidadTransporte().getTipoTransporte(), t.getCantidad()))
                        .toList();
                model.addAttribute("opciones", sugerencias.combinaciones());
                model.addAttribute("claveActual", Combinacion.claveDe(actuales));
                model.addAttribute("factorPct", sugerencias.factorAprovechamiento()
                        .multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP));
            }
        }
        return "pedido_detalles";
    }

    // ------------------------------------------------------------ FINALIZAR / ELIMINAR
    @PostMapping("/{id}/finalizar")
    public String finalizar(@PathVariable Long id, RedirectAttributes ra) {
        Pedido pedido = pedidoService.findById(id);
        if (contieneService.findByPedidoId(id).isEmpty()) {
            ra.addFlashAttribute("error", "No se puede finalizar un pedido sin productos.");
            return "redirect:/pedidos/editar/" + id;
        }
        if (transportaService.findByPedidoId(id).isEmpty()) {
            ra.addFlashAttribute("error", "Calcula el pedido antes de finalizarlo.");
            return "redirect:/pedidos/editar/" + id;
        }
        pedido.setEstado(Estado.FINALIZADO);
        pedidoService.save(pedido);
        ra.addFlashAttribute("mensaje", "Pedido " + id + " finalizado.");
        return "redirect:/historial";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        pedidoService.eliminarPedidoCompleto(id);
        ra.addFlashAttribute("mensaje", "Pedido " + id + " eliminado.");
        return "redirect:/pedidos";
    }

    // ------------------------------------------------------------ helpers
    private Contiene buscarLinea(Long idPedido, Long idProducto) {
        return contieneService.findByPedidoId(idPedido).stream()
                .filter(c -> c.getProducto().getIdProducto().equals(idProducto))
                .findFirst().orElse(null);
    }

    /**
     * Recalcula Pedido.total (suma de piezas). Como cambió el contenido, el cálculo anterior ya no
     * es válido: se quitan las unidades asignadas y se reinician espacios y peso.
     * Si el pedido estaba FINALIZADO, vuelve a PENDIENTE.
     */
    private void actualizarTotal(Pedido pedido) {
        int total = contieneService.findByPedidoId(pedido.getIdPedido()).stream()
                .mapToInt(Contiene::getTotalProducto).sum();
        pedido.setTotal(total);
        pedido.setEspacioUsado(BigDecimal.ZERO);
        pedido.setEspacioLibre(BigDecimal.ZERO);
        pedido.setPesoTotal(BigDecimal.ZERO);
        transportaService.deleteByPedidoId(pedido.getIdPedido());
        if (pedido.getEstado() == Estado.FINALIZADO) {
            pedido.setEstado(Estado.PENDIENTE);
        }
        pedidoService.save(pedido);
    }
}
