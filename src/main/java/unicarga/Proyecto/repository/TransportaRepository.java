package unicarga.Proyecto.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import unicarga.Proyecto.model.Transporta;

import java.util.List;

public interface TransportaRepository extends JpaRepository<Transporta, Long> {
    @EntityGraph(attributePaths = "unidadTransporte")
    List<Transporta> findByPedido_IdPedido(Long idPedido);

    void deleteByPedido_IdPedido(Long idPedido);
}
