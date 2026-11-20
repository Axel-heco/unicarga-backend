package unicarga.Proyecto.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import unicarga.Proyecto.config.AuthInterceptor;
import unicarga.Proyecto.enums.Estado;
import unicarga.Proyecto.model.*;
import unicarga.Proyecto.service.*;

import java.math.BigDecimal;
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
    private final UnidadTransporteService unidadTransporteService;

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

        // Pedido.unidadTransporte es NOT NULL: mientras no exista el cálculo se asigna
        // una unidad provisional (la primera del catálogo). El cálculo la reemplazará.
        List<UnidadTransporte> unidades = unidadTransporteService.findAll();
        if (unidades.isEmpty()) {
            ra.addFlashAttribute("error", "Registra al menos una unidad de transporte en el Catálogo antes de crear un pedido.");
            return "redirect:/pedidos";
        }

        Pedido pedido = pedidoService.save(Pedido.builder()
                .total(0)
                .espacioUsado(BigDecimal.ZERO)
                .espacioLibre(BigDecimal.ZERO)
                .estado(Estado.PENDIENTE)
                .fechaPedido(LocalDateTime.now())
                .usuario(usuario)
                .unidadTransporte(unidades.get(0))
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
    @GetMapping("/{id}/calcular")
    public String calcular(@PathVariable Long id, RedirectAttributes ra) {
        pedidoService.findById(id);
        if (contieneService.findByPedidoId(id).isEmpty()) {
            ra.addFlashAttribute("error", "Agrega al menos un producto antes de calcular.");
            return "redirect:/pedidos/editar/" + id;
        }

        // TODO (PENDIENTE): cálculo de cubicaje. Aquí debe:
        //   1) calcular espacioUsado / espacioLibre / unidad recomendada,
        //   2) guardarlos en el Pedido (pedidoService.save).
        // Por ahora solo muestra los valores almacenados.
        return "redirect:/pedidos/" + id + "/detalles";
    }

    @GetMapping("/{id}/detalles")
    public String detalles(@PathVariable Long id, Model model) {
        Pedido pedido = pedidoService.findById(id);
        model.addAttribute("pedido", pedido);
        model.addAttribute("detalles", contieneService.findByPedidoId(id));
        model.addAttribute("porcentajeUsado", PedidoUtil.porcentajeUsado(pedido));
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

    /** Recalcula Pedido.total (suma de piezas). Si el pedido estaba FINALIZADO, vuelve a PENDIENTE. */
    private void actualizarTotal(Pedido pedido) {
        int total = contieneService.findByPedidoId(pedido.getIdPedido()).stream()
                .mapToInt(Contiene::getTotalProducto).sum();
        pedido.setTotal(total);
        if (pedido.getEstado() == Estado.FINALIZADO) {
            pedido.setEstado(Estado.PENDIENTE);
        }
        pedidoService.save(pedido);
    }
}
