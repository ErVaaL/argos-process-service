package com.erval.argos.process.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import com.erval.argos.process.application.port.in.StartReportJobUseCase.CreateReportJobCommand;
import com.erval.argos.process.application.port.out.ProcessJobExcelExporterPort;
import com.erval.argos.process.application.port.out.ProcessJobRepositoryPort;
import com.erval.argos.process.application.port.out.ReportRequestPublisherPort;
import com.erval.argos.process.application.port.out.ResourceQueryPort;
import com.erval.argos.process.core.domain.PageRequest;
import com.erval.argos.process.core.domain.PageResult;
import com.erval.argos.process.core.domain.SortDirection;
import com.erval.argos.process.core.domain.job.JobStatus;
import com.erval.argos.process.core.domain.job.JobType;
import com.erval.argos.process.core.domain.job.ProcessJob;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private ProcessJobRepositoryPort repo;

    @Mock
    private ResourceQueryPort resourceQuery;

    @Mock
    private ReportRequestPublisherPort publisher;

    @Mock
    private ProcessJobExcelExporterPort excelExporter;

    @Test
    void createReportJob_defaultsDatesAndPublishes() {
        var device = new ResourceQueryPort.DeviceInfo("dev-1", "Device One", true, true);
        when(resourceQuery.getDevice("dev-1")).thenReturn(device);
        when(repo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var service = new JobService(repo, resourceQuery, publisher, excelExporter);
        var cmd = new CreateReportJobCommand("dev-1", null, null);

        ProcessJob saved = service.createReportJob(cmd);

        assertNotNull(saved);
        assertEquals(JobStatus.REQUESTED, saved.status());
        assertEquals(JobType.REPORT_PDF, saved.type());

        var fromCaptor = ArgumentCaptor.forClass(String.class);
        var toCaptor = ArgumentCaptor.forClass(String.class);
        verify(publisher).reportRequested(anyString(), anyString(), fromCaptor.capture(), toCaptor.capture());

        Instant from = Instant.parse(fromCaptor.getValue());
        Instant to = Instant.parse(toCaptor.getValue());
        Duration range = Duration.between(from, to);
        assertTrue(range.toHours() >= 23 && range.toHours() <= 25);
    }

    @Test
    void createReportJob_rejectsMissingDevice() {
        when(resourceQuery.getDevice("dev-404"))
                .thenReturn(new ResourceQueryPort.DeviceInfo(null, null, false, false));

        var service = new JobService(repo, resourceQuery, publisher, excelExporter);
        var cmd = new CreateReportJobCommand("dev-404", "2024-01-01T00:00:00Z", "2024-01-02T00:00:00Z");

        var ex = assertThrows(IllegalArgumentException.class, () -> service.createReportJob(cmd));
        assertTrue(ex.getMessage().contains("Device not found"));
    }

    @Test
    void createReportJob_rejectsInactiveDevice() {
        when(resourceQuery.getDevice("dev-2"))
                .thenReturn(new ResourceQueryPort.DeviceInfo("dev-2", "Device Two", false, true));

        var service = new JobService(repo, resourceQuery, publisher, excelExporter);
        var cmd = new CreateReportJobCommand("dev-2", "2024-01-01T00:00:00Z", "2024-01-02T00:00:00Z");

        var ex = assertThrows(IllegalArgumentException.class, () -> service.createReportJob(cmd));
        assertTrue(ex.getMessage().contains("Device inactive"));
    }

    @Test
    void exportPerformedJobs_filtersByStatus() {
        var job1 = new ProcessJob("1", JobType.REPORT_PDF, "dev-1", "Device", JobStatus.DONE, Instant.now());
        var job2 = new ProcessJob("2", JobType.REPORT_PDF, "dev-2", "Device", JobStatus.FAILED, Instant.now());
        when(repo.findAll()).thenReturn(List.of(job1, job2));
        when(excelExporter.export(anyList())).thenReturn(new byte[] { 1, 2, 3 });

        var service = new JobService(repo, resourceQuery, publisher, excelExporter);
        service.exportPerformedJobs("done");

        var captor = ArgumentCaptor.forClass(List.class);
        verify(excelExporter).export(captor.capture());
        assertEquals(1, captor.getValue().size());
    }

    @Test
    void exportPerformedJobs_keepsAllForUnknownFilter() {
        var job1 = new ProcessJob("1", JobType.REPORT_PDF, "dev-1", "Device", JobStatus.DONE, Instant.now());
        var job2 = new ProcessJob("2", JobType.REPORT_PDF, "dev-2", "Device", JobStatus.FAILED, Instant.now());
        when(repo.findAll()).thenReturn(List.of(job1, job2));
        when(excelExporter.export(anyList())).thenReturn(new byte[] { 9 });

        var service = new JobService(repo, resourceQuery, publisher, excelExporter);
        service.exportPerformedJobs("nope");

        var captor = ArgumentCaptor.forClass(List.class);
        verify(excelExporter).export(captor.capture());
        assertEquals(2, captor.getValue().size());
    }

    @Test
    void listReportJobs_delegatesToRepository() {
        var pageRequest = new PageRequest(0, 10, "createdAt", SortDirection.DESC);
        var job = new ProcessJob("1", JobType.REPORT_PDF, "dev", "Device", JobStatus.REQUESTED, Instant.now());
        var page = new PageResult<>(List.of(job), 1, 0, 10);
        when(repo.findAll(pageRequest)).thenReturn(page);

        var service = new JobService(repo, resourceQuery, publisher, excelExporter);
        PageResult<ProcessJob> result = service.listReportJobs(pageRequest);

        assertSame(page, result);
    }
}
