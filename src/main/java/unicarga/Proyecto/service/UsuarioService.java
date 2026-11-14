package unicarga.Proyecto.service;

import unicarga.Proyecto.model.Usuario;

public interface UsuarioService {
    // Permitir al Usuario loggearse
    Usuario login(String correoUsuario, String contraUsuario);
}