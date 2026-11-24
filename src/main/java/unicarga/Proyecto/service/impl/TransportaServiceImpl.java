package unicarga.Proyecto.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unicarga.Proyecto.model.Transporta;
import unicarga.Proyecto.repository.TransportaRepository;
import unicarga.Proyecto.service.TransportaService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransportaServiceImpl implements TransportaService {
    private final TransportaRepository transportaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Transporta> findByPedidoId(Long idPedido) {
        return transportaRepository.findByPedido_IdPedido(idPedido);
    }

    @Override
    @Transactional
    public List<Transporta> saveAll(List<Transporta> asignaciones) {
        return transportaRepository.saveAll(asignaciones);
    }

    @Override
    @Transactional
    public void deleteByPedidoId(Long idPedido) {
        transportaRepository.deleteByPedido_IdPedido(idPedido);
    }
}
