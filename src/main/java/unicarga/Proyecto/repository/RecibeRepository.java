package unicarga.Proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicarga.Proyecto.model.Recibe;

@Repository
public interface RecibeRepository extends JpaRepository<Recibe, Long>{
}