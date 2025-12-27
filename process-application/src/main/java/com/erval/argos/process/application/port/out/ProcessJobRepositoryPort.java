package com.erval.argos.process.application.port.out;

import java.util.Optional;

import com.erval.argos.process.core.domain.job.ProcessJob;

public interface ProcessJobRepositoryPort {
    ProcessJob save(ProcessJob job);

    Optional<ProcessJob> findById(String id);
}
