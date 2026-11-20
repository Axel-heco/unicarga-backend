package unicarga.Proyecto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/** Errores de las pantallas Thymeleaf: muestra templates/error.html */
@ControllerAdvice(basePackages = "unicarga.Proyecto.controller.web")
public class WebExceptionHandler {

    @ExceptionHandler(ResourcesNotFoundException.class)
    public ModelAndView noEncontrado(ResourcesNotFoundException ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(HttpStatus.NOT_FOUND);
        mav.addObject("mensaje", ex.getMessage());
        return mav;
    }
}
