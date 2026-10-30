package unicarga.Proyecto.Modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Usuario")
public class Usuario {
    @Id
    @Column(name = "idUsuario", nullable = false, unique = true, length = 10)
    private String idUsuario;

    @Column(name = "nombreUsuario", nullable = false, length = 100)
    private String nombreUsuario;

    @Column(name = "contraUsuario", nullable = false, length = 200)
    private String contraUsuario;

    @Column(name = "correoUsuario", nullable = false, length = 150, unique = true)
    private String correoUsuario;

    @JoinColumn(name = "idRol", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Rol rol;
}
