package unicarga.Proyecto.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unicarga.Proyecto.exceptions.ResourcesNotFoundException;
import unicarga.Proyecto.model.UnidadTransporte;
import unicarga.Proyecto.service.UnidadTransporteService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transportes")
@RequiredArgsConstructor
public class UnidadTransporteRestController {

    private final UnidadTransporteService unidadTransporteService;

    @GetMapping
    public List<UnidadTransporte> listar() {
        return unidadTransporteService.findAll();
    }

    @GetMapping("/{id}")
    public UnidadTransporte obtener(@PathVariable Long id) {
        return unidadTransporteService.findById(id);
    }

    @GetMapping("/tipo/{tipo}")
    public UnidadTransporte porTipo(@PathVariable String tipo) {
        return unidadTransporteService.findByTipoTransporte(tipo);
    }

    @GetMapping("/{id}/volumen")
    public BigDecimal volumen(@PathVariable Long id) {
        return unidadTransporteService.calcularVolumen(unidadTransporteService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UnidadTransporte crear(@RequestBody UnidadTransporte unidad) {
        unidad.setIdUnidadTransporte(null);
        return unidadTransporteService.save(unidad);
    }

    @PutMapping("/{id}")
    public UnidadTransporte actualizar(@PathVariable Long id, @RequestBody UnidadTransporte unidad) {
        unidadTransporteService.findById(id);
        unidad.setIdUnidadTransporte(id);
        return unidadTransporteService.save(unidad);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        if (!unidadTransporteService.existsById(id)) {
            throw new ResourcesNotFoundException("No se encontro la UnidadTransporte");
        }
        unidadTransporteService.deleteById(id);
    }
}
