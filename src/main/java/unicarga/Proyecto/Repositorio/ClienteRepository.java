package unicarga.Proyecto.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicarga.Proyecto.Modelo.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
}