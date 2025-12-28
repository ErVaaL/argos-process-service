package com.erval.argos.process.bootstrap.web;

import com.erval.argos.process.application.port.in.StartReportJobUseCase;
import com.erval.argos.process.core.domain.job.ProcessJob;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProcessController {

    private final StartReportJobUseCase useCase;

    @PostMapping("/jobs/report")
    public ProcessJob start(@RequestBody StartReportJobUseCase.CreateReportJobCommand cmd) {
        return useCase.createReportJob(cmd);
    }
}
