package unicarga.Proyecto.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicarga.Proyecto.Modelo.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>{
}