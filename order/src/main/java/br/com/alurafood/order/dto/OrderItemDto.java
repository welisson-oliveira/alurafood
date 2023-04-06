package br.com.alurafood.order.dto;

import br.com.alurafood.order.model.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class OrderItemDto {
    private Long id;

    @NotNull
    @Positive
    private Integer quantity;

    private String description;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Order order;
}
