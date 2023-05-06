package br.com.alurafood.order.controller;

import br.com.alurafood.order.dto.OrderDto;
import br.com.alurafood.order.dto.StatusDto;
import br.com.alurafood.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
@Slf4j
public class OrderController {
    private final OrderService service;

    @GetMapping("/port")
    public String getPort(@Value("${local.server.port}") final String port) {
        OrderController.log.info(String.format("Requisição respondida pela instância executando na porta %s", port));
        return String.format("Requisição respondida pela instância executando na porta %s", port);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable @NotNull final Long id) {
        final OrderDto dto = this.service.getById(id);

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody @Valid final OrderDto dto, final UriComponentsBuilder uriBuilder) {
        final OrderDto requestedOrders = this.service.createOrder(dto);

        final URI address = uriBuilder.path("/order/{id}").buildAndExpand(requestedOrders.getId()).toUri();

        return ResponseEntity.created(address).body(requestedOrders);

    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDto> updateStatus(@PathVariable final Long id, @RequestBody final StatusDto status) {
        final OrderDto dto = this.service.updateStatus(id, status);

        return ResponseEntity.ok(dto);
    }


    @PutMapping("/{id}/paid")
    public ResponseEntity<Void> approvePaymentOrder(@PathVariable @NotNull final Long id) {
        this.service.approvePaymentOrder(id);

        return ResponseEntity.ok().build();

    }
}
