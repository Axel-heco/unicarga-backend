package unicarga.Proyecto.Modelo;

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
@Table(name = "Producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProducto", nullable = false, unique = true)
    private Long idProducto;

    @Column(name = "nombreProducto",  nullable = false, length = 150)
    private String nombreProducto;

    @Column(name = "categoria",  nullable = false, length = 100)
    private String categoria;

    @Column(name = "largoProducto",  nullable = false, precision = 10, scale = 2)
    private BigDecimal largoProducto;

    @Column(name = "anchoProducto",  nullable = false, precision = 10, scale = 2)
    private BigDecimal anchoProducto;

    @Column(name = "altoProducto",  nullable = false, precision = 10, scale = 2)
    private BigDecimal altoProducto;

    @Column(name = "pesoProducto",  nullable = false, precision = 10, scale = 2)
    private BigDecimal pesoProducto;
}
