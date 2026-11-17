package unicarga.Proyecto.service;

import unicarga.Proyecto.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    List<Cliente> findAll();

    //Consultar Ciente por ID
    Optional<Cliente> findClienteById(long id);

    Optional<Cliente> findClienteByEmail(String email);
}
