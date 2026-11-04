package unicarga.Proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicarga.Proyecto.model.UnidadTransporte;

@Repository
public interface UnidadTransporteRepository extends JpaRepository<UnidadTransporte, Long> {
}
