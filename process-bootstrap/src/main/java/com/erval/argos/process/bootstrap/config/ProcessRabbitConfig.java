package com.erval.argos.process.bootstrap.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessRabbitConfig {

    @Bean
    JacksonJsonMessageConverter jacksonConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory cf, JacksonJsonMessageConverter conv) {
        var t = new RabbitTemplate(cf);
        t.setMessageConverter(conv);
        return t;
    }

    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf,
            JacksonJsonMessageConverter conv) {
        var f = new SimpleRabbitListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setMessageConverter(conv);
        return f;
    }

    @Bean
    DirectExchange argosEventsExchange() {
        return new DirectExchange("argos.events");
    }

    @Bean
    Queue reportGeneratedQueue() {
        return new Queue("report.generated.v1", true);
    }

    @Bean
    Queue reportFailedQueue() {
        return new Queue("report.failed.v1", true);
    }

    @Bean
    Binding bindReportGenerated(Queue reportGeneratedQueue, DirectExchange argosEventsExchange) {
        return BindingBuilder.bind(reportGeneratedQueue)
                .to(argosEventsExchange)
                .with("report.generated.v1");
    }

    @Bean
    Binding bindReportFailed(Queue reportFailedQueue, DirectExchange argosEventsExchange) {
        return BindingBuilder.bind(reportFailedQueue)
                .to(argosEventsExchange)
                .with("report.failed.v1");
    }
}
