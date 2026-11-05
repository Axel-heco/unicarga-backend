package unicarga.Proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicarga.Proyecto.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
}