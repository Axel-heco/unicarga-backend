package unicarga.Proyecto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Estado {
    PENDIENTE("Pendiente"),
    FINALIZADO("Finalizado");

    private String descripcion;
}
