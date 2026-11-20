package unicarga.Proyecto.service.impl;

import unicarga.Proyecto.model.Recibe;
import unicarga.Proyecto.repository.RecibeRepository;
import unicarga.Proyecto.service.RecibeService;
import unicarga.Proyecto.exceptions.ResourcesNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecibeServiceImpl implements RecibeService
{
    private final RecibeRepository recibeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Recibe> findAll()
    {
        return recibeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Recibe findById(Long id) {
        return recibeRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("No se encontro el recibe"));
    }

    @Override
    @Transactional
    public Recibe save(Recibe recibe)
    {
        return recibeRepository.save(recibe);
    }

    @Override
    @Transactional
    public void deleteById(Long id)
    {
        recibeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id)
    {
        return recibeRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Recibe> findByPedidoId(Long idPedido) {
        return recibeRepository.findFirstByPedido_IdPedido(idPedido);
    }
}
