package unicarga.Proyecto.service.impl;

import unicarga.Proyecto.model.Producto;
import unicarga.Proyecto.repository.ProductoRepository;
import unicarga.Proyecto.service.ProductoService;
import unicarga.Proyecto.exceptions.ResourcesNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService
{
    private final ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findAll()
    {
        return productoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Producto findById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("No se encontro el producto"));
    }

    @Override
    @Transactional
    public Producto save(Producto Producto)
    {
        return productoRepository.save(Producto);
    }

    @Override
    @Transactional
    public void deleteById(Long id)
    {
        productoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id)
    {
        return productoRepository.existsById(id);
    }

    @Override
    public BigDecimal calcularVolumen(Producto producto) {
        BigDecimal altoProducto = producto.getAltoProducto();
        BigDecimal anchoProducto = producto.getAnchoProducto();
        BigDecimal largoProducto = producto.getLargoProducto();
        return altoProducto.multiply(anchoProducto).multiply(largoProducto);
    }

    @Override
    public List<Producto> findByCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }
}
