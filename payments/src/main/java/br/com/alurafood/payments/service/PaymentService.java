package br.com.alurafood.payments.service;

import br.com.alurafood.payments.dto.PaymentDto;
import br.com.alurafood.payments.httpclient.OrderClient;
import br.com.alurafood.payments.model.Payment;
import br.com.alurafood.payments.model.Status;
import br.com.alurafood.payments.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository repository;
    private final ModelMapper modelMapper;

    private final OrderClient order;

    public Page<PaymentDto> getAll(final Pageable paging) {
        return this.repository
                .findAll(paging)
                .map(p -> this.modelMapper.map(p, PaymentDto.class));
    }

    public PaymentDto getById(final Long id) {
        final Payment payment = this.repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return this.modelMapper.map(payment, PaymentDto.class);
    }

    public PaymentDto createPayment(final PaymentDto dto) {
        final Payment payment = this.modelMapper.map(dto, Payment.class);
        payment.setStatus(Status.CREATED);
        this.repository.save(payment);

        return this.modelMapper.map(payment, PaymentDto.class);
    }

    public PaymentDto updatePayment(final Long id, final PaymentDto dto) {
        Payment payment = this.modelMapper.map(dto, Payment.class);
        payment.setId(id);
        payment = this.repository.save(payment);
        return this.modelMapper.map(payment, PaymentDto.class);
    }

    public void deletePayment(final Long id) {
        this.repository.deleteById(id);
    }

    public void confirmOrder(final Long id) {
        final Optional<Payment> payment = this.repository.findById(id);

        if (payment.isEmpty()) {
            throw new EntityNotFoundException();
        }

        payment.get().setStatus(Status.CONFIRMED);
        this.repository.save(payment.get());
        this.order.updateOrder(payment.get().getOrderId());
    }

    public void authorizedPaymentPendingIntegration(final Long id) {
        final Optional<Payment> payment = this.repository.findById(id);

        if (payment.isEmpty()) {
            throw new EntityNotFoundException();
        }

        payment.get().setStatus(Status.CONFIRMED_WITHOUT_INTEGRATION);
        this.repository.save(payment.get());

    }
}
