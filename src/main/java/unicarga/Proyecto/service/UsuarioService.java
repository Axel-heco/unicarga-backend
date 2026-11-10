package unicarga.Proyecto.service;

import unicarga.Proyecto.model.Usuario;

public interface UsuarioService {
    Usuario login(String nombreUsuario, String contraUsuario);

    Usuario findById(Long id);

    Usuario findAll();

    Usuario findByNombreUsuario(String nombreUsuario);
}