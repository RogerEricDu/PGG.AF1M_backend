package big.data.bigdata.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResultFileGenerator {
    private static final String STORAGE_DIR = "/your/storage/path/";

    public static File writeResultToFile(List<Map<String,Object>> snpData, Long recordId, String dataType) throws IOException {
        String filename = STORAGE_DIR + "result_" + recordId + (dataType.equals("NGS")? ".xlsx": ".txt" );
        File resultFile = new File(filename);

        if ( filename.endsWith(".xlsx")){
            writeExcel(resultFile, snpData);
        }else {
            writeTxt(resultFile, snpData);
        }
        return resultFile;
    }

    private static void writeExcel(File file, List<Map<String, Object>> snpData) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SNP Result");
        int rowNum = 0;

        // 写 header
        if (!snpData.isEmpty()) {
            Row headerRow = sheet.createRow(rowNum++);
            List<String> headers = new ArrayList<>(snpData.get(0).keySet());
            for (int i = 0; i < headers.size(); i++) {
                headerRow.createCell(i).setCellValue(headers.get(i));
            }
        }

        // 写内容
        for (Map<String, Object> rowData : snpData) {
            Row row = sheet.createRow(rowNum++);
            int col = 0;
            for (Object val : rowData.values()) {
                row.createCell(col++).setCellValue(val != null ? val.toString() : "");
            }
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    private static void writeTxt(File file, List<Map<String, Object>> snpData) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            if (!snpData.isEmpty()) {
                List<String> headers = new ArrayList<>(snpData.get(0).keySet());
                writer.write(String.join("\t", headers));
                writer.newLine();
            }

            for (Map<String, Object> row : snpData) {
                List<String> line = row.values().stream()
                        .map(val -> val != null ? val.toString() : "")
                        .collect(Collectors.toList());
                writer.write(String.join("\t", line));
                writer.newLine();
            }
        }
    }
}
