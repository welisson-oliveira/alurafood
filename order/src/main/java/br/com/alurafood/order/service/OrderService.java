package br.com.alurafood.order.service;

import br.com.alurafood.order.dto.OrderDto;
import br.com.alurafood.order.dto.StatusDto;
import br.com.alurafood.order.model.Order;
import br.com.alurafood.order.model.Status;
import br.com.alurafood.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final ModelMapper modelMapper;

    public List<OrderDto> getAll() {
        return this.repository.findAll().stream()
                .map(p -> this.modelMapper.map(p, OrderDto.class)).toList();
    }

    public OrderDto getById(final Long id) {
        final Order order = this.repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return this.modelMapper.map(order, OrderDto.class);
    }

    public OrderDto createOrder(final OrderDto dto) {
        final Order order = this.modelMapper.map(dto, Order.class);

        order.setDateTime(LocalDateTime.now());
        order.setStatus(Status.CREATED);
        order.getItems().forEach(item -> item.setOrder(order));

        this.repository.save(order);

        return this.modelMapper.map(order, OrderDto.class);
    }

    public OrderDto updateStatus(final Long id, final StatusDto dto) {

        final Order order = this.repository.getByIdWithItems(id);

        if (order == null) {
            throw new EntityNotFoundException();
        }

        order.setStatus(dto.getStatus());
        this.repository.updateStatus(dto.getStatus(), order);
        return this.modelMapper.map(order, OrderDto.class);
    }

    public void approvePaymentOrder(final Long id) {

        final Order order = this.repository.getByIdWithItems(id);

        if (order == null) {
            throw new EntityNotFoundException();
        }

        order.setStatus(Status.PAID);
        this.repository.updateStatus(Status.PAID, order);
    }
}
