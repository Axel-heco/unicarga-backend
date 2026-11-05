package unicarga.Proyecto.service.impl;

import unicarga.Proyecto.model.Pedido;
import unicarga.Proyecto.repository.PedidoRepository;
import unicarga.Proyecto.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService
{
    private final PedidoRepository PedidoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findAll()
    {
        return PedidoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> findById(Long id)
    {
        return PedidoRepository.findById(id);
    }   

    @Override
    @Transactional
    public Pedido save(Pedido Pedido)
    {
        return PedidoRepository.save(Pedido);
    }

    @Override
    @Transactional
    public void deleteById(Long id)
    {
        PedidoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id)
    {
        return PedidoRepository.existsById(id);
    }
}
