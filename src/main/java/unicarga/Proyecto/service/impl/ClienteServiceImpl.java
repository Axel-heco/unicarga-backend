package unicarga.Proyecto.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unicarga.Proyecto.model.Cliente;
import unicarga.Proyecto.repository.ClienteRepository;
import unicarga.Proyecto.service.ClienteService;
import unicarga.Proyecto.exceptions.ResourcesNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return  clienteRepository.findAll();
    }

    @Override
    @Transactional
    public Cliente findClienteById(long id) {
        return clienteRepository.findById(id)
                .orElseThrow(()-> new ResourcesNotFoundException("Cliente no encontrado"));
    }

    @Override
    @Transactional
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente findByEmpresa(String empresa) {
        return clienteRepository.findByEmpresa(empresa);
    }

    @Override
    public void deleteById(long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public boolean existById(long id) {
        return clienteRepository.existsById(id);
    }
}
