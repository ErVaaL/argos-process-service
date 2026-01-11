package com.erval.argos.process.application.port.out;

import java.util.Optional;
import java.util.List;

import com.erval.argos.process.core.domain.PageRequest;
import com.erval.argos.process.core.domain.PageResult;
import com.erval.argos.process.core.domain.job.ProcessJob;

/**
 * Port for persisting and retrieving process jobs.
 */
public interface ProcessJobRepositoryPort {
    /**
     * Saves a job aggregate.
     *
     * @param job job to persist
     * @return saved job
     */
    ProcessJob save(ProcessJob job);

    /**
     * Finds a job by id.
     *
     * @param id job identifier
     * @return optional job
     */
    Optional<ProcessJob> findById(String id);

    /**
     * Lists jobs using pagination settings.
     *
     * @param pageRequest paging and sorting settings
     * @return page of jobs
     */
    PageResult<ProcessJob> findAll(PageRequest pageRequest);

    /**
     * Lists all jobs without paging.
     *
     * @return all jobs
     */
    List<ProcessJob> findAll();
}
