package unicarga.Proyecto.service;

import unicarga.Proyecto.model.UnidadTransporte;

public interface UnidadTransporteService {

    UnidadTransporte findById(Long id);

    UnidadTransporte create(UnidadTransporte unidadTransporte);
}
