package unicarga.Proyecto.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicarga.Proyecto.Modelo.UnidadTransporte;

@Repository
public interface UnidadTransporteRepository extends JpaRepository<UnidadTransporte, Long> {
}
