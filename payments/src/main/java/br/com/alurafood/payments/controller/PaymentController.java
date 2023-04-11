package br.com.alurafood.payments.controller;

import br.com.alurafood.payments.dto.PaymentDto;
import br.com.alurafood.payments.service.PaymentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService service;

    @GetMapping
    public Page<PaymentDto> getAll(@PageableDefault(size = 10) final Pageable paging) {
        return this.service.getAll(paging);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> detail(@PathVariable @NotNull final Long id) {
        final PaymentDto dto = this.service.getById(id);

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@RequestBody @Valid final PaymentDto dto, final UriComponentsBuilder uriBuilder) {
        final PaymentDto payment = this.service.createPayment(dto);
        final URI address = uriBuilder.path("/payment/{id}").buildAndExpand(payment.getId()).toUri();

        return ResponseEntity.created(address).body(payment);
    }

    @PostMapping("/fanout")
    public ResponseEntity<PaymentDto> createPaymentFanout(@RequestBody @Valid final PaymentDto dto, final UriComponentsBuilder uriBuilder) {
        final PaymentDto payment = this.service.createPaymentFanout(dto);
        final URI address = uriBuilder.path("/payment/{id}").buildAndExpand(payment.getId()).toUri();

        return ResponseEntity.created(address).body(payment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDto> update(@PathVariable @NotNull final Long id, @RequestBody @Valid final PaymentDto dto) {
        final PaymentDto updated = this.service.updatePayment(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PaymentDto> delete(@PathVariable @NotNull final Long id) {
        this.service.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirm")
    @CircuitBreaker(name = "confirmOrder", fallbackMethod = "authorizedPaymentPendingIntegration")
    public void confirmOrder(@PathVariable @NotNull final Long id) {
        this.service.confirmOrder(id);
    }

    @SuppressWarnings("unused")
    private void authorizedPaymentPendingIntegration(final Long id, final Exception e) {
        this.service.authorizedPaymentPendingIntegration(id);
    }
}
