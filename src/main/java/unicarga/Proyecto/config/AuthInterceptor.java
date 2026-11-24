package unicarga.Proyecto.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * Bloquea cualquier ruta si no hay un Usuario en sesión.
 * - Rutas web  -> redirige a /login
 * - Rutas /api -> responde 401 en JSON
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    public static final String SESSION_USUARIO = "usuario";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(SESSION_USUARIO) != null) {
            return true;
        }
        String ctx = request.getContextPath();
        if (request.getRequestURI().startsWith(ctx + "/api/")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"No autenticado. Inicia sesión primero.\"}");
        } else {
            response.sendRedirect(ctx + "/login");
        }
        return false;
    }
}
