package unicarga.Proyecto.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unicarga.Proyecto.exceptions.ResourcesNotFoundException;
import unicarga.Proyecto.model.Usuario;
import unicarga.Proyecto.repository.UsuarioRepository;
import unicarga.Proyecto.service.UsuarioService;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public Usuario login(String correoUsuario, String contraUsuario) {
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new ResourcesNotFoundException("Email o contraseña incorrectos"));
        if (!usuario.getContraUsuario().equals(contraUsuario)) {
            throw new ResourcesNotFoundException("Email o contraseña incorrectos");
        }
        return usuario;
    }
}
