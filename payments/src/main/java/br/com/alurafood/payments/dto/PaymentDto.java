package br.com.alurafood.payments.dto;

import br.com.alurafood.payments.model.Status;
import lombok.*;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class PaymentDto {

    private Long id;
    private BigDecimal value;
    private String name;
    private String number;
    private String expiration;
    private String code;
    private Status status;
    private Long orderId;
    private Long paymentMethodId;
}
