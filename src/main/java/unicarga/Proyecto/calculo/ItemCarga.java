package unicarga.Proyecto.calculo;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Línea de carga de un pedido: un producto y cuántas piezas se envían.
 * Medidas en centímetros y peso en kilogramos (por pieza).
 */
public record ItemCarga(String nombre,
                        BigDecimal largo,
                        BigDecimal ancho,
                        BigDecimal alto,
                        BigDecimal peso,
                        int cantidad) {

    /** Volumen de UNA pieza en cm³. */
    public BigDecimal volumenUnitarioCm3() {
        return largo.multiply(ancho).multiply(alto);
    }

    /** Volumen de todas las piezas de la línea en cm³. */
    public BigDecimal volumenTotalCm3() {
        return volumenUnitarioCm3().multiply(BigDecimal.valueOf(cantidad));
    }

    /** Peso de todas las piezas de la línea en kg. */
    public BigDecimal pesoTotalKg() {
        return peso.multiply(BigDecimal.valueOf(cantidad));
    }

    /**
     * ¿Cabe UNA pieza en la unidad? Se comparan las dimensiones ordenadas de menor a mayor,
     * lo que equivale a poder girar la pieza en cualquier orientación. También se exige
     * que la pieza individual no supere el peso máximo de la unidad.
     */
    public boolean cabeEn(TipoUnidad unidad) {
        if (peso.compareTo(unidad.pesoMaximo()) > 0) {
            return false;
        }
        BigDecimal[] pieza = ordenadas(largo, ancho, alto);
        BigDecimal[] caja = ordenadas(unidad.largo(), unidad.ancho(), unidad.alto());
        for (int i = 0; i < 3; i++) {
            if (pieza[i].compareTo(caja[i]) > 0) {
                return false;
            }
        }
        return true;
    }

    private static BigDecimal[] ordenadas(BigDecimal a, BigDecimal b, BigDecimal c) {
        BigDecimal[] dims = {a, b, c};
        Arrays.sort(dims);
        return dims;
    }
}
