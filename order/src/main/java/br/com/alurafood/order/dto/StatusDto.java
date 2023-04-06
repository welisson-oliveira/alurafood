package br.com.alurafood.order.dto;

import br.com.alurafood.order.model.Status;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatusDto {
    private Status status;
}
