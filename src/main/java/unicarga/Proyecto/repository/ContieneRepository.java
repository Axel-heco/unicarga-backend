package unicarga.Proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicarga.Proyecto.model.Contiene;

import java.util.List;

public interface ContieneRepository extends JpaRepository<Contiene, Long> {
    List<Contiene> findByPedido_IdPedido(Long idPedido);

    void deleteByPedido_IdPedido(Long idPedido);
}