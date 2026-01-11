package com.erval.argos.process.adapters.rabbitmq.listener;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import com.erval.argos.process.application.port.out.ProcessJobRepositoryPort;
import com.erval.argos.process.core.domain.job.JobStatus;
import com.erval.argos.process.core.domain.job.JobType;
import com.erval.argos.process.core.domain.job.ProcessJob;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportResultListenerTest {

    @Mock
    private ProcessJobRepositoryPort repo;

    @Test
    void onGenerate_updatesStatusToDone() {
        var job = new ProcessJob("job-1", JobType.REPORT_PDF, "dev", "Device", JobStatus.REQUESTED, Instant.now());
        when(repo.findById("job-1")).thenReturn(Optional.of(job));

        var listener = new ReportResultListener(repo);
        listener.onGenerate(Map.of("jobId", "job-1"));

        var captor = ArgumentCaptor.forClass(ProcessJob.class);
        verify(repo).save(captor.capture());
        org.junit.jupiter.api.Assertions.assertEquals(JobStatus.DONE, captor.getValue().status());
    }

    @Test
    void onFailed_updatesStatusToFailed() {
        var job = new ProcessJob("job-2", JobType.REPORT_PDF, "dev", "Device", JobStatus.REQUESTED, Instant.now());
        when(repo.findById("job-2")).thenReturn(Optional.of(job));

        var listener = new ReportResultListener(repo);
        listener.onFailed(Map.of("jobId", "job-2"));

        var captor = ArgumentCaptor.forClass(ProcessJob.class);
        verify(repo).save(captor.capture());
        org.junit.jupiter.api.Assertions.assertEquals(JobStatus.FAILED, captor.getValue().status());
    }
}
