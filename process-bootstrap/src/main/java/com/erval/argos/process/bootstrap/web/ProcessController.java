package com.erval.argos.process.bootstrap.web;

import com.erval.argos.process.application.port.in.StartReportJobUseCase;
import com.erval.argos.process.application.service.JobService;
import com.erval.argos.process.bootstrap.dto.ProcessJobDto;
import com.erval.argos.process.core.domain.PageRequest;
import com.erval.argos.process.core.domain.PageResult;
import com.erval.argos.process.core.domain.SortDirection;
import com.erval.argos.process.core.domain.job.ProcessJob;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * REST controller exposing report job operations.
 */
@RestController
@RequiredArgsConstructor
public class ProcessController {

    private final JobService useCase;

    @PostMapping("/jobs/report")
    public ProcessJob start(@RequestBody StartReportJobUseCase.CreateReportJobCommand cmd) {
        return useCase.createReportJob(cmd);
    }

    @GetMapping("/jobs/list")
    public ResponseEntity<PageResult<ProcessJobDto>> listJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "DESC") SortDirection direction) {
        var pageRequest = new PageRequest(page, size, sortBy, direction);
        PageResult<ProcessJob> jobs = useCase.listReportJobs(pageRequest);
        PageResult<ProcessJobDto> jobDtos = jobs.content().stream().map(ProcessJobDto::fromDomain).toList()
                .stream()
                .collect(PageResult.collector(jobs.totalElements(), jobs.page(), jobs.size()));
        return ResponseEntity.ok(jobDtos);
    }

    @GetMapping("/jobs/export")
    public ResponseEntity<?> exportPerformedJobs(@RequestParam String filter) {
        byte[] payload = useCase.exportPerformedJobs(filter);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"process-jobs.xlsx\"")
                .body(payload);
    }
}
