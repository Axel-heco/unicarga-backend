package unicarga.Proyecto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Rol {
    EMPLEADO("Empleado"),
    ADMINISTRADOR("Administrador");

    private String descripcion;
}
