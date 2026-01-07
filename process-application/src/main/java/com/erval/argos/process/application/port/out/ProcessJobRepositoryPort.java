package com.erval.argos.process.application.port.out;

import java.util.Optional;
import java.util.List;

import com.erval.argos.process.core.domain.PageRequest;
import com.erval.argos.process.core.domain.PageResult;
import com.erval.argos.process.core.domain.job.ProcessJob;

public interface ProcessJobRepositoryPort {
    ProcessJob save(ProcessJob job);

    Optional<ProcessJob> findById(String id);

    PageResult<ProcessJob> findAll(PageRequest pageRequest);

    List<ProcessJob> findAll();
}
