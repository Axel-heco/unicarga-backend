package unicarga.Proyecto.service.impl;

import unicarga.Proyecto.model.Recibe;
import unicarga.Proyecto.repository.RecibeRepository;
import unicarga.Proyecto.service.RecibeService;
import unicarga.Proyecto.exceptions.ResourcesNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecibeServiceImpl implements RecibeService
{
    private final RecibeRepository RecibeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Recibe> findAll()
    {
        return RecibeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Recibe findById(Long id) {
        return RecibeRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("No se encontro el recibe"));
    }

    @Override
    @Transactional
    public Recibe save(Recibe Recibe)
    {
        return RecibeRepository.save(Recibe);
    }

    @Override
    @Transactional
    public void deleteById(Long id)
    {
        RecibeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id)
    {
        return RecibeRepository.existsById(id);
    }
}
