package com.erval.argos.process.adapters.excel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.erval.argos.process.application.port.out.ProcessJobExcelExporterPort;
import com.erval.argos.process.core.domain.job.ProcessJob;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel exporter that renders process jobs into a worksheet.
 */
public class ExcelProcessJobExporterAdapter implements ProcessJobExcelExporterPort {

    private static final String[] HEADERS = {
        "Id",
        "Type",
        "Device Id",
        "Device Name",
        "Status",
        "Created At"
    };

    /**
     * Exports the jobs list into a single-sheet XLSX document.
     *
     * @param jobs jobs to export
     * @return XLSX bytes
     * @throws IllegalStateException when the document cannot be written
     */
    @Override
    public byte[] export(List<ProcessJob> jobs) {
        try (var workbook = new XSSFWorkbook(); var output = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("process-jobs");
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                headerRow.createCell(i).setCellValue(HEADERS[i]);
            }

            int rowIndex = 1;
            for (ProcessJob job : jobs) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(job.id());
                row.createCell(1).setCellValue(job.type().name());
                row.createCell(2).setCellValue(job.deviceId());
                row.createCell(3).setCellValue(job.deviceName());
                row.createCell(4).setCellValue(job.status().name());
                row.createCell(5).setCellValue(job.createdAt().toString());
            }

            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(output);
            return output.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to export process jobs to Excel", e);
        }
    }
}
