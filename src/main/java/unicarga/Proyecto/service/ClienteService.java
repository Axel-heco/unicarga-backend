package unicarga.Proyecto.service;

import unicarga.Proyecto.model.Cliente;
import java.util.List;

public interface ClienteService {

    //Consultar la lista completa de clientes
    List<Cliente> findAll();

    //Consultar Ciente por ID
    Cliente findClienteById(long id);

    //Guardar Cliente
    Cliente save(Cliente cliente);

    //Buscar Cliente por Empresa
    Cliente findByEmpresa(String empresa);

    //Borrar cliente por ID
    void deleteById(long id);

    //Busca si existe un cliente por ID
    boolean existById(long id);

}
