package unicarga.Proyecto.service;

import unicarga.Proyecto.calculo.ResultadoCalculo;

public interface CalculoService {
    /** Calcula las combinaciones de unidades para el pedido SIN guardar nada. */
    ResultadoCalculo sugerir(Long idPedido);

    /**
     * Calcula y guarda en el pedido la combinación elegida (1 = la recomendada):
     * reemplaza sus unidades (Transporta) y actualiza espacioUsado, espacioLibre y pesoTotal.
     * Si el cálculo no es viable no modifica nada y lo informa en el resultado.
     */
    ResultadoCalculo calcularYAplicar(Long idPedido, int opcion);
}
