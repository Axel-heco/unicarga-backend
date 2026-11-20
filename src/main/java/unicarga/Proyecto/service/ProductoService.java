package unicarga.Proyecto.service;


import unicarga.Proyecto.model.Producto;
import unicarga.Proyecto.model.UnidadTransporte;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductoService
{
    // Consultar todos los Productos
    List<Producto> findAll();
    // Consultar un Producto por ID
    Producto findById(Long id);
    // Guardar o actualizar un Producto
    Producto save(Producto Producto);
    // Eliminar un Producto por ID
    void deleteById(Long id);
    // Verificar si existe un Producto
    boolean existsById(Long id);
    // Calcular Volumen de Producto
    BigDecimal calcularVolumen(Producto producto);
    // Consultar por categoria
    List<Producto> findByCategoria(String categoria);
}
