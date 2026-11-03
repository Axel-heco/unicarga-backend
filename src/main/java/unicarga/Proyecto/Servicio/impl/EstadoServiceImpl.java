package unicarga.Proyecto.Servicio.impl;

import unicarga.Proyecto.Modelo.Estado;
import unicarga.Proyecto.Repositorio.EstadoRepository;
import unicarga.Proyecto.Servicio.EstadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EstadoServiceImpl implements EstadoService
{
    private final EstadoRepository EstadoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Estado> findAll()
    {
        return EstadoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Estado> findById(Long id)
    {
        return EstadoRepository.findById(id);
    }

    @Override
    @Transactional
    public Estado save(Estado Estado)
    {
        return EstadoRepository.save(Estado);
    }

    @Override
    @Transactional
    public void deleteById(Long id)
    {
        EstadoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id)
    {
        return EstadoRepository.existsById(id);
    }
}
