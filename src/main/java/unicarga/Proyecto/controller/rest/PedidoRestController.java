package unicarga.Proyecto.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unicarga.Proyecto.enums.Estado;
import unicarga.Proyecto.exceptions.ResourcesNotFoundException;
import unicarga.Proyecto.model.Contiene;
import unicarga.Proyecto.model.Pedido;
import unicarga.Proyecto.model.Recibe;
import unicarga.Proyecto.service.ContieneService;
import unicarga.Proyecto.service.PedidoService;
import unicarga.Proyecto.service.RecibeService;

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

    /** Cuerpo esperado: usuario {idUsuario} y unidadTransporte {idUnidadTransporte} son obligatorios. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido crear(@RequestBody Pedido pedido) {
        pedido.setIdPedido(null);
        if (pedido.getEstado() == null) pedido.setEstado(Estado.PENDIENTE);
        if (pedido.getFechaPedido() == null) pedido.setFechaPedido(LocalDateTime.now());
        if (pedido.getEspacioUsado() == null) pedido.setEspacioUsado(BigDecimal.ZERO);
        if (pedido.getEspacioLibre() == null) pedido.setEspacioLibre(BigDecimal.ZERO);
        return pedidoService.save(pedido);
    }

    @PutMapping("/{id}")
    public Pedido actualizar(@PathVariable Long id, @RequestBody Pedido pedido) {
        pedidoService.findById(id);
        pedido.setIdPedido(id);
        return pedidoService.save(pedido);
    }

    /** Elimina el pedido junto con sus productos y su cliente asignado. */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        pedidoService.eliminarPedidoCompleto(id);
    }
}
