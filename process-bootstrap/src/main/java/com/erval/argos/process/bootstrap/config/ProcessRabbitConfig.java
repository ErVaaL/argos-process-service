package com.erval.argos.process.bootstrap.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declares RabbitMQ infrastructure beans for the process service.
 */
@Configuration
public class ProcessRabbitConfig {

    /**
     * JSON converter for message payloads.
     *
     * @return Jackson message converter
     */
    @Bean
    JacksonJsonMessageConverter jacksonConverter() {
        return new JacksonJsonMessageConverter();
    }

    /**
     * Rabbit template configured with the JSON converter.
     *
     * @param cf connection factory
     * @param conv message converter
     * @return configured rabbit template
     */
    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory cf, JacksonJsonMessageConverter conv) {
        var t = new RabbitTemplate(cf);
        t.setMessageConverter(conv);
        return t;
    }

    /**
     * Listener container factory with JSON conversion.
     *
     * @param cf connection factory
     * @param conv message converter
     * @return listener container factory
     */
    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf,
            JacksonJsonMessageConverter conv) {
        var f = new SimpleRabbitListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setMessageConverter(conv);
        return f;
    }

    /**
     * Exchange for Argos events.
     *
     * @return direct exchange
     */
    @Bean
    DirectExchange argosEventsExchange() {
        return new DirectExchange("argos.events");
    }

    /**
     * Queue for successful report generation events.
     *
     * @return durable queue
     */
    @Bean
    Queue reportGeneratedQueue() {
        return new Queue("report.generated.v1", true);
    }

    /**
     * Queue for failed report generation events.
     *
     * @return durable queue
     */
    @Bean
    Queue reportFailedQueue() {
        return new Queue("report.failed.v1", true);
    }

    /**
     * Binds the generated queue to the events exchange.
     *
     * @param reportGeneratedQueue queue bean
     * @param argosEventsExchange exchange bean
     * @return binding
     */
    @Bean
    Binding bindReportGenerated(Queue reportGeneratedQueue, DirectExchange argosEventsExchange) {
        return BindingBuilder.bind(reportGeneratedQueue)
                .to(argosEventsExchange)
                .with("report.generated.v1");
    }

    /**
     * Binds the failed queue to the events exchange.
     *
     * @param reportFailedQueue queue bean
     * @param argosEventsExchange exchange bean
     * @return binding
     */
    @Bean
    Binding bindReportFailed(Queue reportFailedQueue, DirectExchange argosEventsExchange) {
        return BindingBuilder.bind(reportFailedQueue)
                .to(argosEventsExchange)
                .with("report.failed.v1");
    }
}
