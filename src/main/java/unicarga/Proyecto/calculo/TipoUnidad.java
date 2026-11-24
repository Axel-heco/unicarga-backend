package unicarga.Proyecto.calculo;

import java.math.BigDecimal;

/**
 * Tipo de unidad de transporte del catálogo.
 * Medidas internas en centímetros; pesoMaximo es la carga máxima que soporta, en kg.
 */
public record TipoUnidad(Long id,
                         String nombre,
                         BigDecimal largo,
                         BigDecimal ancho,
                         BigDecimal alto,
                         BigDecimal pesoMaximo) {

    /** Volumen físico en cm³. */
    public BigDecimal volumenCm3() {
        return largo.multiply(ancho).multiply(alto);
    }
}
