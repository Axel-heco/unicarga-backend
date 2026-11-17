package unicarga.Proyecto.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unicarga.Proyecto.exceptions.ResourcesNotFoundException;
import unicarga.Proyecto.model.UnidadTransporte;
import unicarga.Proyecto.repository.UnidadTransporteRepository;
import unicarga.Proyecto.service.UnidadTransporteService;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UnidadTransporteServiceImpl implements UnidadTransporteService {
    private final UnidadTransporteRepository unidadTransporteRepository;

    @Override
    public List<UnidadTransporte> findAll() {
        return unidadTransporteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UnidadTransporte findById(Long id) {
        return unidadTransporteRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("No se encontro la UnidadTransporte"));
    }

    @Override
    public UnidadTransporte save(UnidadTransporte unidadTransporte) {
        return unidadTransporteRepository.save(unidadTransporte);
    }

    @Override
    public void deleteById(Long id) {
        unidadTransporteRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return unidadTransporteRepository.existsById(id);
    }

    @Override
    public UnidadTransporte findByTipoTransporte(String tipoTransporte) {
        return unidadTransporteRepository.findByTipoTransporte(tipoTransporte)
                .orElseThrow(() -> new ResourcesNotFoundException("Unidad transporte no encontrado"));
    }

    @Override
    public BigDecimal calcularVolumen(UnidadTransporte unidadTransporte) {
        BigDecimal altoTransporte = unidadTransporte.getAltoTransporte();
        BigDecimal anchoTransporte = unidadTransporte.getAnchoTransporte();
        BigDecimal largoTransporte = unidadTransporte.getLargoTransporte();
        return altoTransporte.multiply(anchoTransporte).multiply(largoTransporte);
    }
}
