package unicarga.Proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unicarga.Proyecto.enums.Estado;
import unicarga.Proyecto.model.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findByFechaPedido(LocalDateTime fechaPedido);

    List<Pedido> findByEstadoOrderByFechaPedidoDesc(Estado estado);

    /**
     * UPDATE explícito del resultado del cálculo: se ejecuta siempre, sin depender de que Hibernate
     * detecte cambios en una entidad. Devuelve cuántas filas actualizó (1 si el pedido existe).
     */
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Pedido p set p.espacioUsado = :usado, p.espacioLibre = :libre, p.pesoTotal = :peso "
            + "where p.idPedido = :id")
    int actualizarCalculo(@Param("id") Long id,
                          @Param("usado") BigDecimal usado,
                          @Param("libre") BigDecimal libre,
                          @Param("peso") BigDecimal peso);
}
