package unicarga.Proyecto.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unicarga.Proyecto.model.Contiene;
import unicarga.Proyecto.service.ContieneService;

import java.util.List;

@RestController
@RequestMapping("/api/contiene")
@RequiredArgsConstructor
public class ContieneRestController {

    private final ContieneService contieneService;

    /** GET /api/contiene  |  GET /api/contiene?pedidoId=5 */
    @GetMapping
    public List<Contiene> listar(@RequestParam(required = false) Long pedidoId) {
        return pedidoId == null ? contieneService.findAll() : contieneService.findByPedidoId(pedidoId);
    }

    @GetMapping("/{id}")
    public Contiene obtener(@PathVariable long id) {
        return contieneService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contiene crear(@RequestBody Contiene contiene) {
        contiene.setIdContiene(null);
        return contieneService.save(contiene);
    }

    @PutMapping("/{id}")
    public Contiene actualizar(@PathVariable long id, @RequestBody Contiene contiene) {
        contieneService.findById(id);
        contiene.setIdContiene(id);
        return contieneService.save(contiene);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable long id) {
        contieneService.delete(contieneService.findById(id));
    }
}
