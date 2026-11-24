package unicarga.Proyecto.calculo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Resultado de calcular el transporte de un pedido.
 *
 * @param viable                  false si no existe ninguna combinación válida (ver motivo)
 * @param motivo                  explicación cuando viable = false; null en caso contrario
 * @param volumenTotalM3          volumen de toda la carga en m³
 * @param pesoTotalKg             peso de toda la carga en kg
 * @param factorAprovechamiento   fracción del volumen físico que se considera utilizable (0-1]
 * @param combinaciones           opciones ordenadas de mejor a peor; la primera es la recomendada
 */
public record ResultadoCalculo(boolean viable,
                               String motivo,
                               BigDecimal volumenTotalM3,
                               BigDecimal pesoTotalKg,
                               BigDecimal factorAprovechamiento,
                               List<Combinacion> combinaciones) {

    public static ResultadoCalculo noViable(String motivo, BigDecimal volumenTotalM3,
                                            BigDecimal pesoTotalKg, BigDecimal factor) {
        return new ResultadoCalculo(false, motivo, volumenTotalM3, pesoTotalKg, factor, List.of());
    }
}
