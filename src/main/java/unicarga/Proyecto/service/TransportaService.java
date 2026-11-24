package unicarga.Proyecto.service;

import unicarga.Proyecto.model.Transporta;

import java.util.List;

public interface TransportaService {
    // Unidades de transporte asignadas a un Pedido (con su cantidad)
    List<Transporta> findByPedidoId(Long idPedido);
    // Guardar varias asignaciones
    List<Transporta> saveAll(List<Transporta> asignaciones);
    // Quitar todas las unidades asignadas a un Pedido
    void deleteByPedidoId(Long idPedido);
}
