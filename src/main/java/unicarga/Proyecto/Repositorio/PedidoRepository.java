package unicarga.Proyecto.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicarga.Proyecto.Modelo.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
