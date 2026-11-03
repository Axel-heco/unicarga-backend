package unicarga.Proyecto.Servicio;


import unicarga.Proyecto.Modelo.Estado;

import java.util.List;
import java.util.Optional;

public interface EstadoService
{
    // Consultar todos los Estados
    List<Estado> findAll();
    // Consultar un Estado por ID
    Optional<Estado> findById(Long id);
    // Guardar o actualizar un Estado
    Estado save(Estado Estado);
    // Eliminar un Estado por ID
    void deleteById(Long id);
    // Verificar si existe un Estado
    boolean existsById(Long id);
}
