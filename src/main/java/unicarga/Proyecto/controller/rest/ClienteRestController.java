package unicarga.Proyecto.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unicarga.Proyecto.exceptions.ResourcesNotFoundException;
import unicarga.Proyecto.model.Cliente;
import unicarga.Proyecto.service.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteRestController {

    private final ClienteService clienteService;

    @GetMapping
    public List<Cliente> listar() {
        return clienteService.findAll();
    }

    @GetMapping("/{id}")
    public Cliente obtener(@PathVariable long id) {
        return clienteService.findClienteById(id);
    }

    @GetMapping("/empresa/{empresa}")
    public Cliente porEmpresa(@PathVariable String empresa) {
        return clienteService.findByEmpresa(empresa);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente crear(@RequestBody Cliente cliente) {
        cliente.setIdCliente(null);
        return clienteService.save(cliente);
    }

    @PutMapping("/{id}")
    public Cliente actualizar(@PathVariable long id, @RequestBody Cliente cliente) {
        clienteService.findClienteById(id);
        cliente.setIdCliente(id);
        return clienteService.save(cliente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable long id) {
        if (!clienteService.existById(id)) {
            throw new ResourcesNotFoundException("Cliente no encontrado");
        }
        clienteService.deleteById(id);
    }
}
