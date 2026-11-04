package unicarga.Proyecto.Servicio;


import unicarga.Proyecto.Modelo.Recibe;

import java.util.List;
import java.util.Optional;

public interface RecibeService
{
    // Consultar todos los Recibes
    List<Recibe> findAll();
    // Consultar un Recibe por ID
    Optional<Recibe> findById(Long id);
    // Guardar o actualizar un Recibe
    Recibe save(Recibe Recibe);
    // Eliminar un Recibe por ID
    void deleteById(Long id);
    // Verificar si existe un Recibe
    boolean existsById(Long id);
}
