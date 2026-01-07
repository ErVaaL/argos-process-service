package com.erval.argos.process.application.port.out;

import java.util.List;

import com.erval.argos.process.core.domain.job.ProcessJob;

public interface ProcessJobExcelExporterPort {
    byte[] export(List<ProcessJob> jobs);
}
