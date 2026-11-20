package unicarga.Proyecto.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import unicarga.Proyecto.enums.Estado;
import unicarga.Proyecto.model.Pedido;
import unicarga.Proyecto.service.ContieneService;
import unicarga.Proyecto.service.PedidoService;

import java.util.List;

/** Historial: pedidos FINALIZADOS. */
@Controller
@RequestMapping("/historial")
@RequiredArgsConstructor
public class HistorialController {

    private final PedidoService pedidoService;
    private final ContieneService contieneService;

    @GetMapping
    public String listar(@RequestParam(required = false) String idPedido, Model model) {
        List<Pedido> pedidos = pedidoService.findByEstado(Estado.FINALIZADO);
        if (idPedido != null && !idPedido.isBlank()) {
            String filtro = idPedido.trim();
            pedidos = pedidos.stream()
                    .filter(p -> String.valueOf(p.getIdPedido()).contains(filtro))
                    .toList();
        }
        model.addAttribute("historialPedidos", pedidos);
        model.addAttribute("idPedido", idPedido);
        return "historial";
    }

    @GetMapping("/{id}")
    public String detalles(@PathVariable Long id, Model model) {
        Pedido pedido = pedidoService.findById(id);
        model.addAttribute("pedido", pedido);
        model.addAttribute("detalles", contieneService.findByPedidoId(id));
        model.addAttribute("porcentajeUsado", PedidoUtil.porcentajeUsado(pedido));
        return "historial_detalles";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        pedidoService.eliminarPedidoCompleto(id);
        ra.addFlashAttribute("mensaje", "Pedido " + id + " eliminado del historial.");
        return "redirect:/historial";
    }
}
