package unicarga.Proyecto.service;


import unicarga.Proyecto.model.Pedido;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoService
{
    // Consultar todos los Pedidos
    List<Pedido> findAll();
    // Consultar un Pedido por ID
    Pedido findById(Long id);
    // Guardar o actualizar un Pedido
    Pedido save(Pedido Pedido);
    // Eliminar un Pedido por ID
    void deleteById(Long id);
    // Verificar si existe un Pedido
    boolean existsById(Long id);
    // Consultar por fecha un Pedido
    Pedido findByFechaPedido(LocalDateTime fechaPedido);

}
