package br.com.alurafood.order.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AmqpConfig {

    private final AmqpAdmin amqpAdmin;

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory, final Jackson2JsonMessageConverter messageConverter) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> initAdmin(final RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("payment.ex");
    }

    @Bean
    public FanoutExchange deadLetterExchange() {
        return new FanoutExchange("payment.dlx");
    }

    @Bean
    public Queue orderDetailQueue() {
        return new Queue("payment.order-detail", true, false, false);
    }

    @Bean
    public Queue evaluationQueue() {
        return QueueBuilder.durable("payment.order-evaluation")
                .deadLetterExchange("payment.dlx").build();
    }

    @Bean
    public Queue evaluationDlqQueue() {
        return new Queue("payment.order-evaluation-dlq", true, false, false);
    }

    @Bean
    public Binding bindPaymentOrder(final FanoutExchange fanoutExchange) {
        final Queue queue = this.orderDetailQueue();
        return new Binding(queue.getName(), Binding.DestinationType.QUEUE, fanoutExchange.getName(), queue.getName(), null);
    }

    @Bean
    public Binding bindEvaluationOrder() {
        final Queue queue = this.evaluationQueue();
        return new Binding(queue.getName(), Binding.DestinationType.QUEUE, this.fanoutExchange().getName(), queue.getName(), null);
    }

    @Bean
    public Binding bindEvaluationOrderDlq() {
        final Queue queue = this.evaluationDlqQueue();
        return new Binding(queue.getName(), Binding.DestinationType.QUEUE, this.deadLetterExchange().getName(), queue.getName(), null);
    }

}
