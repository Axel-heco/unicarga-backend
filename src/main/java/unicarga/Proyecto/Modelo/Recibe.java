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
@Table(name = "Recibe")
public class Recibe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRecibe", nullable = false, unique = true)
    private Long idRecibe;

    @JoinColumn(name = "idPedido", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Pedido pedido;

    @JoinColumn(name = "idCliente", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;
}
