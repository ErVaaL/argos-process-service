package com.erval.argos.process.application.port.out;

public interface ReportRequestPublisherPort {
    void reportRequested(String jobId, String deviceId, String from, String to);
}
