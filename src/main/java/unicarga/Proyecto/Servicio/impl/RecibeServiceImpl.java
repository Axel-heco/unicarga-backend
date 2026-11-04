package unicarga.Proyecto.Servicio.impl;

import unicarga.Proyecto.Modelo.Recibe;
import unicarga.Proyecto.Repositorio.RecibeRepository;
import unicarga.Proyecto.Servicio.RecibeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public Optional<Recibe> findById(Long id)
    {
        return RecibeRepository.findById(id);
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
