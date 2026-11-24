package unicarga.Proyecto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Relación Pedido - UnidadTransporte: qué unidades llevan el pedido y cuántas de cada tipo
 * (p. ej. 2 × Camión). Un pedido puede tener varias filas.
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Transporta")
public class Transporta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTransporta", nullable = false, unique = true)
    private Long idTransporta;

    @JsonIgnore
    @JoinColumn(name = "idPedido", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Pedido pedido;

    @JoinColumn(name = "idTransporte", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UnidadTransporte unidadTransporte;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;
}
