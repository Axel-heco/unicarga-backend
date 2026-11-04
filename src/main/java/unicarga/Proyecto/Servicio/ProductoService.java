package unicarga.Proyecto.Servicio;


import unicarga.Proyecto.Modelo.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService
{
    // Consultar todos los Productos
    List<Producto> findAll();
    // Consultar un Producto por ID
    Optional<Producto> findById(Long id);
    // Guardar o actualizar un Producto
    Producto save(Producto Producto);
    // Eliminar un Producto por ID
    void deleteById(Long id);
    // Verificar si existe un Producto
    boolean existsById(Long id);
}
