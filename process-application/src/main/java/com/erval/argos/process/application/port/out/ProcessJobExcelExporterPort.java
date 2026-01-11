package com.erval.argos.process.application.port.out;

import java.util.List;

import com.erval.argos.process.core.domain.job.ProcessJob;

/**
 * Port for exporting process jobs to Excel.
 */
public interface ProcessJobExcelExporterPort {
    /**
     * Exports the provided jobs as an Excel document.
     *
     * @param jobs jobs to include in the document
     * @return Excel bytes
     */
    byte[] export(List<ProcessJob> jobs);
}
