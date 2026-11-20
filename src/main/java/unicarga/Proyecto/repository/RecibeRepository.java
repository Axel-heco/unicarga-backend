package unicarga.Proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicarga.Proyecto.model.Recibe;

import java.util.Optional;

public interface RecibeRepository extends JpaRepository<Recibe, Long> {
    Optional<Recibe> findFirstByPedido_IdPedido(Long idPedido);

    void deleteByPedido_IdPedido(Long idPedido);
}
