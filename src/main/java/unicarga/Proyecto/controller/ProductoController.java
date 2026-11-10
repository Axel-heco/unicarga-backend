package unicarga.Proyecto.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicarga.Proyecto.model.Producto;
import unicarga.Proyecto.service.ProductoService;

import java.util.List;

@Tag(name = "Productos", description = "Operaciones CRUD para la administración de productos y mercancía")
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @Operation(summary = "Consultar todos los productos", description = "Obtiene la lista completa de productos registrados en el sistema")
    @GetMapping
    public ResponseEntity<List<Producto>> findAll() {
        List<Producto> productos = productoService.findAll();
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Consultar un producto por ID", description = "Obtiene un producto específico por su ID único")
    @GetMapping("/{id}")
    public ResponseEntity<Producto> findById(@PathVariable Long id) {
        return productoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto con sus dimensiones y peso en el sistema")
    @PostMapping
    public ResponseEntity<Producto> create(@RequestBody Producto producto) {
        producto.setIdProducto(null); // Asegura que se cree un nuevo registro
        Producto nuevoProducto = productoService.save(producto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevoProducto);
    }

    @Operation(summary = "Actualizar un producto existente", description = "Actualiza los datos, dimensiones o peso de un producto existente")
    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.findById(id)
                .map(productoExistente -> {
                    productoExistente.setNombreProducto(producto.getNombreProducto());
                    productoExistente.setCategoria(producto.getCategoria());
                    productoExistente.setLargoProducto(producto.getLargoProducto());
                    productoExistente.setAnchoProducto(producto.getAnchoProducto());
                    productoExistente.setAltoProducto(producto.getAltoProducto());
                    productoExistente.setPesoProducto(producto.getPesoProducto());

                    Producto actualizado = productoService.save(productoExistente);
                    return ResponseEntity.ok(actualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un producto por ID", description = "Elimina un producto del sistema por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!productoService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}