package unicarga.Proyecto.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unicarga.Proyecto.exceptions.ResourcesNotFoundException;
import unicarga.Proyecto.model.Recibe;
import unicarga.Proyecto.service.RecibeService;

import java.util.List;

@RestController
@RequestMapping("/api/recibe")
@RequiredArgsConstructor
public class RecibeRestController {

    private final RecibeService recibeService;

    @GetMapping
    public List<Recibe> listar() {
        return recibeService.findAll();
    }

    @GetMapping("/{id}")
    public Recibe obtener(@PathVariable Long id) {
        return recibeService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Recibe crear(@RequestBody Recibe recibe) {
        recibe.setIdRecibe(null);
        return recibeService.save(recibe);
    }

    @PutMapping("/{id}")
    public Recibe actualizar(@PathVariable Long id, @RequestBody Recibe recibe) {
        recibeService.findById(id);
        recibe.setIdRecibe(id);
        return recibeService.save(recibe);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        if (!recibeService.existsById(id)) {
            throw new ResourcesNotFoundException("No se encontro el recibe");
        }
        recibeService.deleteById(id);
    }
}
