package com.gerdoc.escuela.service.impl;

import com.gerdoc.escuela.model.Producto;
import com.gerdoc.escuela.repository.ProductoRepository;
import com.gerdoc.escuela.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService
{
    private final ProductoRepository ProductoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findAll()
    {
        return ProductoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> findById(Long id)
    {
        return ProductoRepository.findById(id);
    }

    @Override
    @Transactional
    public Producto save(Producto Producto)
    {
        return ProductoRepository.save(Producto);
    }

    @Override
    @Transactional
    public void deleteById(Long id)
    {
        ProductoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id)
    {
        return ProductoRepository.existsById(id);
    }
}
