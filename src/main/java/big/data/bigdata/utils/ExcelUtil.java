package big.data.bigdata.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    public static List<String[]> readChromosomePosition(File file) throws IOException {
        List<String[]> data = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(file)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell chrCell = row.getCell(0);
                Cell posCell = row.getCell(1);

                String chr = getCellAsString(chrCell);
                String pos = getCellAsString(posCell);

                data.add(new String[]{chr, pos});
            }
        }
        return data;
    }

    private static String getCellAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    public static void writeSNPResult(File file, List<String[]> rows) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SNP_Results");

        // 表头（列标题）
        String[] headers = {
                "Chromosome", "Position", "rsID", "population","Ref Allele", "Alt Allele",
                "Ref Allele Frequency", "Alt Allele Frequency", "Dataset", "Sample Size",
                "Genotype 1", "Genotype Frequency 1", "Genotype 2", "Genotype Frequency 2",
                "Genotype 3", "Genotype Frequency 3", "Variant", "Allele Count"
        };

        // 写入表头
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (String[] rowData : rows) {
            Row row = sheet.createRow(rowIdx++);
            for (int i = 0; i < rowData.length; i++) {
                row.createCell(i).setCellValue(rowData[i]);
            }
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
    }
}

