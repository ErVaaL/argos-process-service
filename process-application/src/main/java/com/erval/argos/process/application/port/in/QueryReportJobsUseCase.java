package com.erval.argos.process.application.port.in;

import com.erval.argos.process.core.domain.PageRequest;
import com.erval.argos.process.core.domain.PageResult;
import com.erval.argos.process.core.domain.job.ProcessJob;

public interface QueryReportJobsUseCase {
  PageResult<ProcessJob> listReportJobs(PageRequest pageRequest);

  byte[] exportPerformedJobs(String filter);
}
