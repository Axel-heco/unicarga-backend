package unicarga.Proyecto.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicarga.Proyecto.Modelo.Contiene;

@Repository
public interface ContieneRepository extends JpaRepository<Contiene, Long>{
}