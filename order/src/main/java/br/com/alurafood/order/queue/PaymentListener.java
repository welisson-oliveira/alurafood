package br.com.alurafood.order.queue;

import br.com.alurafood.order.dto.PaymentDto;
import br.com.alurafood.order.exception.ProcessError;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class PaymentListener {

    @RabbitListener(queues = "payment.concluded")
    public void receiveMessage(final PaymentDto payment) {

        final String message = """
                        Dados do pagamento: %s
                Número do pedido: %s
                Valor R$: %s
                Status: %s 
                     """.formatted(payment.getId(),
                payment.getOrderId(),
                payment.getValue(),
                payment.getStatus());

        PaymentListener.log.info("Recebi a mensagem " + message);
    }

    @RabbitListener(queues = "payment.order-detail")
    public void receiveMessageDetailFanout(final PaymentDto payment) {

        final String message = """
                        Dados do pagamento: %s
                Número do pedido: %s
                Valor R$: %s
                Status: %s 
                     """.formatted(payment.getId(),
                payment.getOrderId(),
                payment.getValue(),
                payment.getStatus());

        PaymentListener.log.info("Recebi a mensagem pelo DETAIL: \n" + message);
    }

    @RabbitListener(queues = "payment.order-evaluation")
    public void receiveMessageEvaluationFanout(@Payload final PaymentDto paymentDto) {
        PaymentListener.log.info(paymentDto.getId());
        PaymentListener.log.info(paymentDto.getNumber());

        if (paymentDto.getNumber().equals("0000")) {
            throw new ProcessError("não consegui processar");
        }

        final String message = """
                Necessário criar registro de avaliação para o pedido: %s 
                Id do pagamento: %s
                Nome do cliente: %s
                Valor R$: %s
                Status: %s 
                """.formatted(paymentDto.getOrderId(),
                paymentDto.getId(),
                paymentDto.getName(),
                paymentDto.getValue(),
                paymentDto.getStatus());

        PaymentListener.log.info("Recebi a mensagem pelo EVALUATION: \n" + message);
    }

}
