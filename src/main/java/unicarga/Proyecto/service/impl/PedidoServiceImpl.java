package unicarga.Proyecto.service.impl;

import unicarga.Proyecto.enums.Estado;
import unicarga.Proyecto.model.Pedido;
import unicarga.Proyecto.repository.ContieneRepository;
import unicarga.Proyecto.repository.PedidoRepository;
import unicarga.Proyecto.repository.RecibeRepository;
import unicarga.Proyecto.service.PedidoService;
import unicarga.Proyecto.exceptions.ResourcesNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService
{
    private final PedidoRepository pedidoRepository;
    private final ContieneRepository contieneRepository;
    private final RecibeRepository recibeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findAll()
    {
        return pedidoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Pedido findById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("No encontrado el pedido"));
    }

    @Override
    @Transactional
    public Pedido save(Pedido pedido)
    {
        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public void deleteById(Long id)
    {
        pedidoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id)
    {
        return pedidoRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Pedido findByFechaPedido(LocalDateTime fechaPedido) {
        return pedidoRepository.findByFechaPedido(fechaPedido)
                .orElseThrow(() -> new ResourcesNotFoundException("No encontrado el pedido"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findByEstado(Estado estado) {
        return pedidoRepository.findByEstadoOrderByFechaPedidoDesc(estado);
    }

    @Override
    @Transactional
    public void eliminarPedidoCompleto(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new ResourcesNotFoundException("No encontrado el pedido");
        }
        contieneRepository.deleteByPedido_IdPedido(id);
        recibeRepository.deleteByPedido_IdPedido(id);
        pedidoRepository.deleteById(id);
    }
}
