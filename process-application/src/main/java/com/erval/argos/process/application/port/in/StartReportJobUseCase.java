package com.erval.argos.process.application.port.in;

import com.erval.argos.process.core.domain.job.ProcessJob;

public interface StartReportJobUseCase {

    ProcessJob createReportJob(CreateReportJobCommand cmd);

    record CreateReportJobCommand(String deviceId, String from, String to) {
    }
}
