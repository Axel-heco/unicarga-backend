package unicarga.Proyecto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import unicarga.Proyecto.enums.Estado;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPedido", nullable = false, unique = true)
    private Long idPedido;

    @Column(name = "total", nullable = false)
    private int total;

    /** Volumen de la carga en m³ (resultado del cálculo). */
    @Column(name = "espacioUsado", nullable = false, precision = 14, scale = 4)
    private BigDecimal espacioUsado;

    /** Capacidad útil sobrante de las unidades asignadas, en m³ (resultado del cálculo). */
    @Column(name = "espacioLibre", nullable = false, precision = 14, scale = 4)
    private BigDecimal espacioLibre;

    /** Peso total de la carga en kg (resultado del cálculo). */
    @Column(name = "pesoTotal", nullable = false, precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal pesoTotal = BigDecimal.ZERO;

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Column(name = "fechaPedido",  nullable = false)
    private LocalDateTime fechaPedido;

    @JoinColumn(name = "idUsuario", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;
}
