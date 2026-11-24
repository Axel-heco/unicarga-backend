package unicarga.Proyecto.calculo;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Una forma posible de transportar el pedido (por ejemplo "1 Camión + 1 Van").
 *
 * @param posicion        lugar en el ranking, empezando en 1 (la 1 es la recomendada)
 * @param recomendada     true solo para la mejor combinación
 * @param capacidadUtilM3 volumen físico total × factor de aprovechamiento
 * @param espacioLibreM3  capacidadUtilM3 - espacioUsadoM3
 */
public record Combinacion(int posicion,
                          boolean recomendada,
                          List<LineaCombinacion> lineas,
                          int totalUnidades,
                          BigDecimal capacidadM3,
                          BigDecimal capacidadUtilM3,
                          BigDecimal espacioUsadoM3,
                          BigDecimal espacioLibreM3,
                          BigDecimal porcentajeUsado,
                          BigDecimal capacidadPesoKg,
                          BigDecimal porcentajePeso) {

    /** Identificador de la composición, p. ej. "1:2,3:1" (idUnidad:cantidad, ordenado por id). */
    public String clave() {
        return claveDe(lineas);
    }

    public static String claveDe(List<LineaCombinacion> lineas) {
        return lineas.stream()
                .sorted(Comparator.comparing(LineaCombinacion::idUnidadTransporte))
                .map(l -> l.idUnidadTransporte() + ":" + l.cantidad())
                .collect(Collectors.joining(","));
    }

    /** Texto legible, p. ej. "2 × Camión + 1 × Van". */
    public String descripcion() {
        return lineas.stream()
                .map(l -> l.cantidad() + " × " + l.tipoTransporte())
                .collect(Collectors.joining(" + "));
    }
}
