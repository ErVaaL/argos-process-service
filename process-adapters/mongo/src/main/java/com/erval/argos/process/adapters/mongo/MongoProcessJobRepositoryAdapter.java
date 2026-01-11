package com.erval.argos.process.adapters.mongo;

import java.util.List;
import java.util.Optional;

import com.erval.argos.process.adapters.mongo.model.ProcessJobDocument;
import com.erval.argos.process.adapters.mongo.repositories.ProcessJobMongoRepository;
import com.erval.argos.process.application.port.out.ProcessJobRepositoryPort;
import com.erval.argos.process.core.domain.PageRequest;
import com.erval.argos.process.core.domain.PageResult;
import com.erval.argos.process.core.domain.job.ProcessJob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * MongoDB-backed adapter for process job persistence.
 * <p>
 * Converts between domain aggregates and Mongo documents while delegating
 * CRUD operations to Spring Data.
 */
@Component
@RequiredArgsConstructor
public class MongoProcessJobRepositoryAdapter implements ProcessJobRepositoryPort {

    private final ProcessJobMongoRepository repo;

    /**
     * Finds a job by id and maps it to the domain aggregate.
     *
     * @param id job identifier
     * @return optional job aggregate
     */
    @Override
    public Optional<ProcessJob> findById(String id) {
        return repo.findById(id)
                .map(ProcessJobDocument::toDomain);
    }

    /**
     * Saves a job aggregate by mapping it to a Mongo document.
     *
     * @param job job aggregate to persist
     * @return saved job aggregate
     */
    @Override
    public ProcessJob save(ProcessJob job) {
        var saved = repo.save(ProcessJobDocument.fromDomain(job));
        return saved.toDomain();
    }

    /**
     * Returns a page of jobs using the provided paging information.
     *
     * @param pageRequest paging settings
     * @return page of jobs
     */
    @Override
    public PageResult<ProcessJob> findAll(PageRequest pageRequest) {
        var pageable = org.springframework.data.domain.PageRequest.of(pageRequest.page(), pageRequest.size());
        var page = repo.findAll(pageable);
        var jobs = page.map(ProcessJobDocument::toDomain).getContent();
        return new PageResult<>(jobs, jobs.size(), pageRequest.page(), pageRequest.size());
    }

    /**
     * Returns all jobs without pagination.
     *
     * @return list of job aggregates
     */
    @Override
    public List<ProcessJob> findAll() {
        return repo.findAll().stream()
            .map(ProcessJobDocument::toDomain)
            .toList();
    }

}
