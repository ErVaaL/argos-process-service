package com.erval.argos.process.adapters.excel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.List;

import com.erval.argos.process.core.domain.job.JobStatus;
import com.erval.argos.process.core.domain.job.JobType;
import com.erval.argos.process.core.domain.job.ProcessJob;

import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;

class ExcelProcessJobExporterAdapterTest {

    @Test
    void export_writesRows() throws Exception {
        var job = new ProcessJob("1", JobType.REPORT_PDF, "dev", "Device", JobStatus.DONE, Instant.parse("2024-01-01T00:00:00Z"));
        var adapter = new ExcelProcessJobExporterAdapter();

        byte[] bytes = adapter.export(List.of(job));

        assertNotNull(bytes);
        try (var workbook = WorkbookFactory.create(new ByteArrayInputStream(bytes))) {
            var sheet = workbook.getSheetAt(0);
            assertEquals("process-jobs", sheet.getSheetName());
            assertEquals("Id", sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("1", sheet.getRow(1).getCell(0).getStringCellValue());
            assertEquals("REPORT_PDF", sheet.getRow(1).getCell(1).getStringCellValue());
        }
    }
}
