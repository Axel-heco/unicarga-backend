package unicarga.Proyecto.service;

import unicarga.Proyecto.model.UnidadTransporte;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UnidadTransporteService {
    // Consultar todos las UnidadTransportes
    List<UnidadTransporte> findAll();
    // Consultar una UnidadTransporte por ID
    Optional<UnidadTransporte> findById(Long id);
    // Guardar o actualizar una UnidadTransporte
    UnidadTransporte save(UnidadTransporte UnidadTransporte);
    // Eliminar una UnidadTransporte por ID
    void deleteById(Long id);
    // Verificar si existe una UnidadTransporte
    boolean existsById(Long id);
    // Consultar una UnidadTransporte por TipoTransporte
    UnidadTransporte findByTipoTransporte(String tipoTransporte);
    // Calcular Volumen de UnidadTransporte
    BigDecimal calcularVolumen(UnidadTransporte unidadTransporte);
}