package unicarga.Proyecto.service.impl;

import unicarga.Proyecto.model.Producto;
import unicarga.Proyecto.repository.ProductoRepository;
import unicarga.Proyecto.service.ProductoService;
import unicarga.Proyecto.exceptions.ResourcesNotFoundException;
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
    public Producto findById(Long id) {
        return ProductoRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("No se encontro el producto"));
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
