package unicarga.Proyecto.controller.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import unicarga.Proyecto.config.AuthInterceptor;
import unicarga.Proyecto.exceptions.ResourcesNotFoundException;
import unicarga.Proyecto.model.Usuario;
import unicarga.Proyecto.service.UsuarioService;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @GetMapping("/")
    public String raiz() {
        // Si no hay sesión, el interceptor lo manda a /login
        return "redirect:/bienvenida";
    }

    @GetMapping("/login")
    public String loginForm(HttpSession session) {
        if (session.getAttribute(AuthInterceptor.SESSION_USUARIO) != null) {
            return "redirect:/bienvenida";
        }
        return "login";
    }

    /** El campo "clave" del formulario es el correo del usuario. */
    @PostMapping("/login")
    public String login(@RequestParam String clave,
                        @RequestParam String contrasena,
                        HttpServletRequest request,
                        Model model) {
        try {
            Usuario usuario = usuarioService.login(clave.trim(), contrasena);

            // Nueva sesión al autenticar (evita session fixation)
            HttpSession anterior = request.getSession(false);
            if (anterior != null) {
                anterior.invalidate();
            }
            request.getSession(true).setAttribute(AuthInterceptor.SESSION_USUARIO, usuario);
            return "redirect:/bienvenida";

        } catch (ResourcesNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            return "login";
        }
    }

    @GetMapping("/bienvenida")
    public String bienvenida(@SessionAttribute(AuthInterceptor.SESSION_USUARIO) Usuario usuario, Model model) {
        model.addAttribute("nombreUsuario", usuario.getNombreUsuario());
        return "bienvenida";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
