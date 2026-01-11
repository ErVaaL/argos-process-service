package com.erval.argos.process.adapters.rabbitmq.publisher;

import java.util.Map;

import com.erval.argos.process.application.port.out.ReportRequestPublisherPort;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * RabbitMQ publisher for report request events.
 */
@Slf4j
@RequiredArgsConstructor
public class RabbitReportRequestPublisherAdapter implements ReportRequestPublisherPort {

    private final RabbitTemplate rabbit;

    private static final String EXCHANGE = "argos.events";
    private static final String RK_REQUESTED = "report.requested.v1";

    /**
     * Publishes a report requested event to the {@code argos.events} exchange.
     *
     * @param jobId    job identifier
     * @param deviceId device identifier
     * @param from     start timestamp (ISO-8601)
     * @param to       end timestamp (ISO-8601)
     */
    @Override
    public void reportRequested(String jobId, String deviceId, String from, String to) {
        log.info("Publishing report requested event for jobId: {}", jobId);
        rabbit.convertAndSend(EXCHANGE, RK_REQUESTED, Map.of(
                "jobId", jobId,
                "deviceId", deviceId,
                "from", from,
                "to", to));
    }

}
