package unicarga.Proyecto.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicarga.Proyecto.Modelo.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {
}
