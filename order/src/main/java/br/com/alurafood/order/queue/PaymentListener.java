package br.com.alurafood.order.queue;

import br.com.alurafood.order.dto.PaymentDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
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

        System.out.println("Recebi a mensagem " + message);
    }

}
