package unicarga.Proyecto.Servicio.impl;

import unicarga.Proyecto.Modelo.Producto;
import unicarga.Proyecto.Repositorio.ProductoRepository;
import unicarga.Proyecto.Servicio.ProductoService;
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
