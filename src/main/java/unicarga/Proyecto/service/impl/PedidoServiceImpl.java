package unicarga.Proyecto.service.impl;

import unicarga.Proyecto.model.Pedido;
import unicarga.Proyecto.repository.PedidoRepository;
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
    public Pedido save(Pedido Pedido)
    {
        return pedidoRepository.save(Pedido);
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
}
