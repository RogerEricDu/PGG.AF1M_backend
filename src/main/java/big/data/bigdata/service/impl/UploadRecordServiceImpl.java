package big.data.bigdata.service.impl;

import big.data.bigdata.entity.UploadRecord;
import big.data.bigdata.mapper.UploadRecordMapper;
import big.data.bigdata.service.UploadRecordService;
import big.data.bigdata.utils.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UploadRecordServiceImpl implements UploadRecordService {

    @Value("${uploadRecord.dir}")
    private String resultDir;

    @Autowired
    private UploadRecordMapper uploadRecordMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 每天凌晨 2 点执行
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanOldRecords() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<UploadRecord> oldRecords = uploadRecordMapper.findOlderThan(oneMonthAgo);

        for (UploadRecord record : oldRecords) {
            try {
                File file = new File(record.getResultPath());
                if (file.exists() && file.getAbsolutePath().startsWith(resultDir)) {
                    if (file.delete()) {
                        System.out.println("Deleted file: " + file.getAbsolutePath());
                    } else {
                        System.out.println("Failed to delete file: " + file.getAbsolutePath());
                    }
                }

                uploadRecordMapper.deleteById(record.getId());
                System.out.println("Deleted upload record: " + record.getId());

            } catch (Exception e) {
                System.err.println("Error deleting record or file: " + e.getMessage());
            }
        }
    }

    //添加一个chromosome的白名单校验，防止恶意注入SQL
    private static final Set<String> VALID_CHROMOSOMES = Set.of(
            "1","2","3","4","5","6","7","8","9","10",
            "11","12","13","14","15","16","17","18","19","20","21","22","X","Y"
    );

    @Override
    public void processExcel(MultipartFile file, String username, String email, String taskName) throws Exception {
        File tempInput = File.createTempFile("upload_", ".xlsx");
        file.transferTo(tempInput);

        List<String[]> inputRows = ExcelUtil.readChromosomePosition(tempInput);
        List<String[]> outputRows = new ArrayList<>();

        for (String[] row : inputRows) {
            String chr = row[0].toLowerCase().replace("chr", "").replace("x", "X").replace("y", "Y");
            if(!VALID_CHROMOSOMES.contains(chr)){
                throw new IllegalArgumentException("Illegal chromosome number:"+ chr);
            }
            String table = "snp_data_chr" + chr.toLowerCase();
            int position = (int) Double.parseDouble(row[1]);
            System.out.println("Querying: " + table + ", position=" + position);


            //防止单条查询失败导致整体失败
            List<Map<String, Object>> snpList;
            try {
                snpList = jdbcTemplate.queryForList(
                        "SELECT * FROM " + table + " WHERE position = ?", position
                );
                System.out.println("查到 " + snpList.size() + " 条结果");
            } catch (Exception e) {
                System.out.println("查询出错: " + table + " position=" + position + "，错误：" + e.getMessage());
                snpList = Collections.emptyList();
            }

            for (Map<String, Object> snp : snpList) {
                String[] outRow = new String[] {
                        chr, row[1],
                        Objects.toString(snp.get("rs_id"), ""),
                        Objects.toString(snp.get("population"), ""),
                        Objects.toString(snp.get("ref_allele"), ""),
                        Objects.toString(snp.get("alt_allele"), ""),
                        Objects.toString(snp.get("ref_allele_frequency"), ""),
                        Objects.toString(snp.get("alt_allele_frequency"), ""),
                        Objects.toString(snp.get("dataset"), ""),
                        Objects.toString(snp.get("sample_size"), ""),
                        Objects.toString(snp.get("genotype1"), ""),
                        Objects.toString(snp.get("genotype_frequency1"), ""),
                        Objects.toString(snp.get("genotype2"), ""),
                        Objects.toString(snp.get("genotype_frequency2"), ""),
                        Objects.toString(snp.get("genotype3"), ""),
                        Objects.toString(snp.get("genotype_frequency3"), ""),
                        Objects.toString(snp.get("variant"), ""),
                        Objects.toString(snp.get("allele_count"), ""),
                        Objects.toString(snp.get("population"), "") // 新增字段 population
                };
                outputRows.add(outRow);
            }
        }

        // 生成输出文件
        String outputPath = resultDir + "result_" + System.currentTimeMillis() + ".xlsx";
        File outputFile = new File(outputPath);
        ExcelUtil.writeSNPResult(outputFile, outputRows);

        // 保存上传记录
        UploadRecord record = new UploadRecord();
        record.setTaskName(taskName);
        record.setUsername(username);
        record.setEmail(email);
        record.setDataType("SNP");
        record.setStatus("Done");
        record.setResultPath(outputPath);
        uploadRecordMapper.insert(record);
    }

    @Override
    public Map<String, Object> getUserTasks(String username) {
        List<Map<String, Object>> tasks = uploadRecordMapper.findByUsername(username);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("data", tasks);
        return response;
    }

    @Override
    public ResponseEntity<Resource> downloadResultFile(Long recordId) throws IOException {
        UploadRecord record = uploadRecordMapper.findById(recordId);
        File file = new File(record.getResultPath());

        if (!file.exists()) {
            throw new FileNotFoundException("Result file not found.");
        }

        FileSystemResource resource = new FileSystemResource(file);
        System.out.println("Downloading file: " + file.getAbsolutePath() + " size: " + file.length());


        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(file.getName(), "UTF-8") + "\"")
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }
}
