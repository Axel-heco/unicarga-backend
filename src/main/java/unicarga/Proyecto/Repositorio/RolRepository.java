package unicarga.Proyecto.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicarga.Proyecto.Modelo.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
}
