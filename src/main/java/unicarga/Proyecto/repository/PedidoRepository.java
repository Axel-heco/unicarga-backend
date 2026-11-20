package unicarga.Proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicarga.Proyecto.enums.Estado;
import unicarga.Proyecto.model.Pedido;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findByFechaPedido(LocalDateTime fechaPedido);

    List<Pedido> findByEstadoOrderByFechaPedidoDesc(Estado estado);
}
