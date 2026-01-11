package com.erval.argos.process.adapters.rabbitmq.publisher;

import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@ExtendWith(MockitoExtension.class)
class RabbitReportRequestPublisherAdapterTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Test
    void reportRequested_sendsEventWithPayload() {
        var adapter = new RabbitReportRequestPublisherAdapter(rabbitTemplate);

        adapter.reportRequested("job-1", "device-1", "2024-01-01T00:00:00Z", "2024-01-02T00:00:00Z");

        var payloadCaptor = ArgumentCaptor.forClass(Map.class);
        verify(rabbitTemplate)
                .convertAndSend(
                        org.mockito.Mockito.eq("argos.events"),
                        org.mockito.Mockito.eq("report.requested.v1"),
                        payloadCaptor.capture());

        Map<String, Object> payload = payloadCaptor.getValue();
        org.junit.jupiter.api.Assertions.assertEquals("job-1", payload.get("jobId"));
        org.junit.jupiter.api.Assertions.assertEquals("device-1", payload.get("deviceId"));
        org.junit.jupiter.api.Assertions.assertEquals("2024-01-01T00:00:00Z", payload.get("from"));
        org.junit.jupiter.api.Assertions.assertEquals("2024-01-02T00:00:00Z", payload.get("to"));
    }
}
