package unicarga.Proyecto.model;

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
@Table(name = "Cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCliente", nullable = false, unique = true)
    private Long idCliente;

    @Column(name = "empresa", nullable = false, length = 150)
    private String empresa;

    @Column(name = "ubicacion", nullable = false, length = 250)
    private String ubicacion;
}
