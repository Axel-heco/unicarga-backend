package unicarga.Proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicarga.Proyecto.model.Contiene;

@Repository
public interface ContieneRepository extends JpaRepository<Contiene, Long>{
}