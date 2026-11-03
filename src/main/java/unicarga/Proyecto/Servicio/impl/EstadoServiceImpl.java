package com.gerdoc.escuela.service.impl;

import com.gerdoc.escuela.model.Estado;
import com.gerdoc.escuela.repository.EstadoRepository;
import com.gerdoc.escuela.service.EstadoService;
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
