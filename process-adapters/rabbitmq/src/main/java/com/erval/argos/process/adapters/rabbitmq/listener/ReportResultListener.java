package com.erval.argos.process.adapters.rabbitmq.listener;

import java.util.Map;

import com.erval.argos.process.application.port.out.ProcessJobRepositoryPort;
import com.erval.argos.process.core.domain.job.JobStatus;

import com.erval.argos.process.core.domain.job.ProcessJob;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReportResultListener {

    private final ProcessJobRepositoryPort repo;

    @RabbitListener(queues = "${argos.rabbitmq.queues.reportGenerated:report.generated.v1}")
    public void onGenerate(Map<String, Object> msg) {
        var jobId = (String) msg.get("jobId");

        var job = repo.findById(jobId).orElseThrow();
        repo.save(job.withStatus(JobStatus.DONE));
    }

    @RabbitListener(queues = "${argos.rabbitmq.queues.reportFailed:report.failed.v1}")
    public void onFailed(Map<String, Object> msg) {
        var jobId = (String) msg.get("jobId");

        ProcessJob job = repo.findById(jobId).orElseThrow();
        repo.save(job.withStatus(JobStatus.FAILED));
    }

}
