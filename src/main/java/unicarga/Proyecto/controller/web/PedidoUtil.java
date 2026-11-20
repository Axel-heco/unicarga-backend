package unicarga.Proyecto.controller.web;

import unicarga.Proyecto.model.Pedido;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Utilidades de presentación para las vistas de pedido. */
final class PedidoUtil {

    private PedidoUtil() {}

    /**
     * Porcentaje de espacio usado a partir de los valores YA guardados en el pedido
     * (usado / (usado + libre)). No es el cálculo de cubicaje: solo formatea lo almacenado.
     */
    static BigDecimal porcentajeUsado(Pedido pedido) {
        BigDecimal capacidad = pedido.getEspacioUsado().add(pedido.getEspacioLibre());
        if (capacidad.signum() == 0) {
            return BigDecimal.ZERO.setScale(1);
        }
        return pedido.getEspacioUsado()
                .multiply(BigDecimal.valueOf(100))
                .divide(capacidad, 1, RoundingMode.HALF_UP);
    }
}
