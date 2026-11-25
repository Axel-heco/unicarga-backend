package unicarga.Proyecto.service;

import unicarga.Proyecto.enums.Estado;
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
    // NUEVO: Consultar Pedidos por estado (PENDIENTE / FINALIZADO), más recientes primero
    List<Pedido> findByEstado(Estado estado);
    // NUEVO: Guardar el resultado del cálculo (espacio usado, espacio libre y peso total) con un UPDATE explícito
    void guardarCalculo(Long id, java.math.BigDecimal espacioUsado, java.math.BigDecimal espacioLibre, java.math.BigDecimal pesoTotal);
    // NUEVO: Eliminar un Pedido junto con sus Contiene, Recibe y Transporta (en una sola transacción)
    void eliminarPedidoCompleto(Long id);
}
