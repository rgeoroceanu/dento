package com.company.dento.report;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class ExcelWriter {

    private static final String REPORT_EXTENSION = ".xlsx";

    public File saveWorkbook(final XSSFWorkbook workbook) throws IOException {
        final File destination = Files.createTempFile("report", REPORT_EXTENSION).toFile();
        final FileOutputStream out = new FileOutputStream(destination);
        workbook.write(out);
        out.close();
        return destination;
    }

    public XSSFWorkbook openTemplate(final File templateFile) throws IOException, InvalidFormatException {
        return new XSSFWorkbook(templateFile);
    }

    public void replaceCellText(final XSSFWorkbook workbook, final String placeholder,
                                final String text, final String cellId) {

        final Cell cell = getCellById(workbook, cellId);
        final String cellText = cell.getStringCellValue();
        final String toWrite = text != null ? text : "";

        if (cellText != null) {
            final String updated = cellText.replace(placeholder, toWrite);
            cell.setCellValue(updated);
        }
    }

    public Cell getCellById(final XSSFWorkbook workbook, final String id) {
        final CellReference cr = new CellReference(id);
        final Row row = workbook.getSheetAt(0).getRow(cr.getRow());
        return row != null ? row.getCell(cr.getCol()) : null;
    }

    public void createTable(final XSSFWorkbook workbook, final int startRow,
                            final List<String> headers, final List<List<String>> rows) {

        final CellReference cr = new CellReference(startRow, 0);
        final int row = cr.getRow();
        final int column = cr.getCol();

        addTableRow(workbook, row, column, headers, true);
        IntStream.range(0, rows.size())
                .forEach(index -> addTableRow(workbook, row + index + 1, column, rows.get(index), false));
    }

    private void addTableRow(final XSSFWorkbook workbook, final int rowIndex,
                              final int startColumnIndex, final List<String> columns,
                              boolean header) {

        final Row row = workbook.getSheetAt(0).createRow(rowIndex);
        final int columnDistance = 18 / columns.size();
        final CellStyle rowStyle = createTableCellStyle(workbook);
        final CellStyle headerStyle = createTableHeaderCellStyle(workbook);

        IntStream.range(0, columns.size())
                .forEach(index -> {
                    final int columnIndex = (startColumnIndex + index) * columnDistance;
                    final Cell cell = row.createCell(columnIndex);
                    //workbook.getSheetAt(0).addMergedRegion(
                    //        new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex + columnDistance - 1));
                    cell.setCellValue(columns.get(index));
                    cell.setCellStyle(header ? headerStyle : rowStyle);
                    workbook.getSheetAt(0).autoSizeColumn(index);
                });
    }

    private CellStyle createTableHeaderCellStyle(final XSSFWorkbook workbook) {
        final CellStyle style = createTableCellStyle(workbook);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFont(createTableHeaderFont(workbook));
        return style;
    }

    private Font createTableHeaderFont(final Workbook workbook) {
        final Font font = workbook.createFont();
        font.setFontHeightInPoints((short)10);
        font.setFontName("Arial");
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);
        font.setItalic(false);
        return font;
    }

    private CellStyle createTableCellStyle(final Workbook workbook) {
        final CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return style;
    }
}
