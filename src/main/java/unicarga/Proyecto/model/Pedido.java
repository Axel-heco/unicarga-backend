package unicarga.Proyecto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import unicarga.Proyecto.enums.Estado;
import unicarga.Proyecto.enums.Rol;

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

    @Column(name = "espacioUsado", nullable = false, precision = 10, scale = 2)
    private BigDecimal espacioUsado;

    @Column(name = "espacioLibre", nullable = false, precision = 10, scale = 2)
    private BigDecimal espacioLibre;

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Column(name = "fechaPedido",  nullable = false)
    private LocalDateTime fechaPedido;

    @JoinColumn(name = "idUsuario", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    @JoinColumn(name = "idUnidad", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UnidadTransporte unidadTransporte;
}
