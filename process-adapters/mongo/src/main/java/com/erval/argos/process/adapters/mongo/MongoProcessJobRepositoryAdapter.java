package com.erval.argos.process.adapters.mongo;

import java.util.Optional;

import com.erval.argos.process.adapters.mongo.model.ProcessJobDocument;
import com.erval.argos.process.adapters.mongo.repositories.ProcessJobMongoRepository;
import com.erval.argos.process.application.port.out.ProcessJobRepositoryPort;
import com.erval.argos.process.core.domain.job.ProcessJob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongoProcessJobRepositoryAdapter implements ProcessJobRepositoryPort {

    private final ProcessJobMongoRepository repo;

    @Override
    public Optional<ProcessJob> findById(String id) {
        return repo.findById(id)
                .map(ProcessJobDocument::toDomain);
    }

    @Override
    public ProcessJob save(ProcessJob job) {
        var saved = repo.save(ProcessJobDocument.fromDomain(job));
        return saved.toDomain();
    }

}
