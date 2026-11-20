package unicarga.Proyecto.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unicarga.Proyecto.model.Contiene;
import unicarga.Proyecto.repository.ContieneRepository;
import unicarga.Proyecto.service.ContieneService;
import unicarga.Proyecto.exceptions.ResourcesNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContieneServiceImpl implements ContieneService {
    private final ContieneRepository contieneRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Contiene> findAll() {
        return contieneRepository.findAll();
    }

    @Override
    public Contiene findById(long id) {
        return contieneRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("Relacion no encontrada"));
    }

    @Override
    public Contiene save(Contiene contiene) {
        return contieneRepository.save(contiene);
    }

    @Override
    public void delete(Contiene contiene) {
        contieneRepository.delete(contiene);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(long id) {
        return contieneRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contiene> findByPedidoId(Long idPedido) {
        return contieneRepository.findByPedido_IdPedido(idPedido);
    }
}
