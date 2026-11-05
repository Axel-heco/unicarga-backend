package unicarga.Proyecto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "UnidadTransporte")
public class UnidadTransporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUnidad", unique = true, nullable = false)
    private Long idUnidadTransporte;

    @Column(name = "tipoTransporte", nullable = false, length = 100)
    private String tipoTransporte;

    @Column(name = "anchoTransporte", nullable = false, precision = 10, scale = 2)
    private BigDecimal anchoTransporte;

    @Column(name = "largoTransporte", nullable = false, precision = 10, scale = 2)
    private BigDecimal  largoTransporte;

    @Column(name = "altoTransporte", nullable = false, precision = 10, scale = 2)
    private BigDecimal  altoTransporte;

    @Column(name = "pesoTransporte", nullable = false, precision = 10, scale = 2)
    private BigDecimal  pesoTransporte;
}
