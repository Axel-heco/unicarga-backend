package unicarga.Proyecto.Servicio.impl;

import unicarga.Proyecto.model.Estado;
import unicarga.Proyecto.repository.EstadoRepository;
import unicarga.Proyecto.Servicio.EstadoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Servicio;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Servicio
@RequiredArgsConstructor
public class EstadoServicioImpl implements EstadoServicio
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
