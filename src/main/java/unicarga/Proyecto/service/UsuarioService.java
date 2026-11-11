package unicarga.Proyecto.service;

import unicarga.Proyecto.model.Usuario;
import java.util.List;

public interface UsuarioService {
    Usuario login(String nombreUsuario, String contraUsuario);
}