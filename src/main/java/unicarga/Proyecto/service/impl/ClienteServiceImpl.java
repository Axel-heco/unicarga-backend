package unicarga.Proyecto.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unicarga.Proyecto.model.Cliente;
import unicarga.Proyecto.repository.ClienteRepository;
import unicarga.Proyecto.service.ClienteService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class ClienteServiceImpl implements ClienteService {

    private ClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return  clienteRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<Cliente> findClienteById(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Cliente> findClienteByEmail(String email) {
        return Optional.empty();
    }
}
