package unicarga.Proyecto.Servicio;


import unicarga.Proyecto.Modelo.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoService
{
    // Consultar todos los Pedidos
    List<Pedido> findAll();
    // Consultar un Pedido por ID
    Optional<Pedido> findById(Long id);
    // Guardar o actualizar un Pedido
    Pedido save(Pedido Pedido);
    // Eliminar un Pedido por ID
    void deleteById(Long id);
    // Verificar si existe un Pedido
    boolean existsById(Long id);
}
