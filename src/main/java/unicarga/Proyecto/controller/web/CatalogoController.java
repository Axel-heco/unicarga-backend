package unicarga.Proyecto.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import unicarga.Proyecto.model.Cliente;
import unicarga.Proyecto.model.Producto;
import unicarga.Proyecto.model.UnidadTransporte;
import unicarga.Proyecto.service.ClienteService;
import unicarga.Proyecto.service.ProductoService;
import unicarga.Proyecto.service.UnidadTransporteService;

import java.math.BigDecimal;
import java.util.List;

/**
 * Catálogo: Productos, Transportes (UnidadTransporte) y Clientes.
 * Crear y actualizar comparten el mismo endpoint /guardar:
 * si el formulario trae el id, se actualiza; si no, se crea.
 */
@Controller
@RequestMapping("/catalogo")
@RequiredArgsConstructor
public class CatalogoController {

    private static final List<String> TABS = List.of("productos", "transportes", "clientes");

    private final ProductoService productoService;
    private final UnidadTransporteService unidadTransporteService;
    private final ClienteService clienteService;

    // ------------------------------------------------------------ LISTADO + BÚSQUEDA
    @GetMapping
    public String catalogo(@RequestParam(defaultValue = "productos") String tab,
                           @RequestParam(required = false) String q,
                           Model model) {
        String tabActiva = TABS.contains(tab) ? tab : "productos";
        String filtro = q == null ? "" : q.trim().toLowerCase();

        switch (tabActiva) {
            case "transportes" -> model.addAttribute("transportes",
                    unidadTransporteService.findAll().stream()
                            .filter(t -> coincide(filtro, t.getIdUnidadTransporte(), t.getTipoTransporte()))
                            .toList());
            case "clientes" -> model.addAttribute("clientes",
                    clienteService.findAll().stream()
                            .filter(c -> coincide(filtro, c.getIdCliente(), c.getEmpresa(), c.getUbicacion()))
                            .toList());
            default -> model.addAttribute("productos",
                    productoService.findAll().stream()
                            .filter(p -> coincide(filtro, p.getIdProducto(), p.getNombreProducto(), p.getCategoria()))
                            .toList());
        }
        model.addAttribute("tabActiva", tabActiva);
        model.addAttribute("q", q);
        return "catalogo";
    }

    // ------------------------------------------------------------ PRODUCTOS
    @PostMapping("/productos/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, RedirectAttributes ra) {
        if (medidasInvalidas(producto.getLargoProducto(), producto.getAnchoProducto(),
                producto.getAltoProducto(), producto.getPesoProducto())) {
            ra.addFlashAttribute("error", "Las medidas y el peso deben ser mayores a cero.");
            return "redirect:/catalogo?tab=productos";
        }
        boolean nuevo = producto.getIdProducto() == null;
        productoService.save(producto);
        ra.addFlashAttribute("mensaje", nuevo ? "Producto creado." : "Producto actualizado.");
        return "redirect:/catalogo?tab=productos";
    }

    @PostMapping("/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes ra) {
        if (!productoService.existsById(id)) {
            ra.addFlashAttribute("error", "El producto no existe.");
        } else {
            try {
                productoService.deleteById(id);
                ra.addFlashAttribute("mensaje", "Producto eliminado.");
            } catch (DataIntegrityViolationException ex) {
                ra.addFlashAttribute("error", "No se puede eliminar: el producto está en uno o más pedidos.");
            }
        }
        return "redirect:/catalogo?tab=productos";
    }

    // ------------------------------------------------------------ TRANSPORTES
    @PostMapping("/transportes/guardar")
    public String guardarTransporte(@ModelAttribute UnidadTransporte unidad, RedirectAttributes ra) {
        if (medidasInvalidas(unidad.getLargoTransporte(), unidad.getAnchoTransporte(),
                unidad.getAltoTransporte(), unidad.getPesoTransporte())) {
            ra.addFlashAttribute("error", "Las medidas y el peso deben ser mayores a cero.");
            return "redirect:/catalogo?tab=transportes";
        }
        boolean nuevo = unidad.getIdUnidadTransporte() == null;
        unidadTransporteService.save(unidad);
        ra.addFlashAttribute("mensaje", nuevo ? "Unidad creada." : "Unidad actualizada.");
        return "redirect:/catalogo?tab=transportes";
    }

    @PostMapping("/transportes/eliminar/{id}")
    public String eliminarTransporte(@PathVariable Long id, RedirectAttributes ra) {
        if (!unidadTransporteService.existsById(id)) {
            ra.addFlashAttribute("error", "La unidad no existe.");
        } else {
            try {
                unidadTransporteService.deleteById(id);
                ra.addFlashAttribute("mensaje", "Unidad eliminada.");
            } catch (DataIntegrityViolationException ex) {
                ra.addFlashAttribute("error", "No se puede eliminar: la unidad está asignada a uno o más pedidos.");
            }
        }
        return "redirect:/catalogo?tab=transportes";
    }

    // ------------------------------------------------------------ CLIENTES
    @PostMapping("/clientes/guardar")
    public String guardarCliente(@ModelAttribute Cliente cliente, RedirectAttributes ra) {
        boolean nuevo = cliente.getIdCliente() == null;
        clienteService.save(cliente);
        ra.addFlashAttribute("mensaje", nuevo ? "Cliente creado." : "Cliente actualizado.");
        return "redirect:/catalogo?tab=clientes";
    }

    @PostMapping("/clientes/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes ra) {
        if (!clienteService.existById(id)) {
            ra.addFlashAttribute("error", "El cliente no existe.");
        } else {
            try {
                clienteService.deleteById(id);
                ra.addFlashAttribute("mensaje", "Cliente eliminado.");
            } catch (DataIntegrityViolationException ex) {
                ra.addFlashAttribute("error", "No se puede eliminar: el cliente tiene pedidos asociados.");
            }
        }
        return "redirect:/catalogo?tab=clientes";
    }

    // ------------------------------------------------------------ helpers
    private boolean coincide(String filtro, Object... campos) {
        if (filtro.isEmpty()) return true;
        for (Object campo : campos) {
            if (campo != null && String.valueOf(campo).toLowerCase().contains(filtro)) return true;
        }
        return false;
    }

    private boolean medidasInvalidas(BigDecimal... valores) {
        for (BigDecimal v : valores) {
            if (v == null || v.signum() <= 0) return true;
        }
        return false;
    }
}
