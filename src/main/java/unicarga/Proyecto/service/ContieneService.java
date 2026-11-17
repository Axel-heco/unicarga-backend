package unicarga.Proyecto.service;

import unicarga.Proyecto.model.Contiene;
import unicarga.Proyecto.model.Recibe;

import java.util.List;

public interface ContieneService {

    //Consultar todos los Contiene
    List<Contiene> findAll();
    //Buscar Contiene por ID
    Contiene findById(long id);
    //Guardar Relación Contiene
    Contiene save(Contiene contiene);
    //Borrar las Relaciones
    void delete(Contiene contiene);

    //Buscar si existen Relaciones Contiene
    boolean existsById(long id);

}
