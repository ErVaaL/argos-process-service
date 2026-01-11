package com.erval.argos.process.application.port.in;

import com.erval.argos.process.core.domain.PageRequest;
import com.erval.argos.process.core.domain.PageResult;
import com.erval.argos.process.core.domain.job.ProcessJob;

/**
 * Use case for querying report jobs and exporting their history.
 */
public interface QueryReportJobsUseCase {
  /**
   * Lists jobs using pagination settings.
   *
   * @param pageRequest paging and sorting settings
   * @return a page of jobs
   */
  PageResult<ProcessJob> listReportJobs(PageRequest pageRequest);

  /**
   * Exports jobs to Excel, optionally filtered by status.
   *
   * @param filter status filter string (case-insensitive) or {@code null}
   * @return Excel document bytes
   */
  byte[] exportPerformedJobs(String filter);
}
