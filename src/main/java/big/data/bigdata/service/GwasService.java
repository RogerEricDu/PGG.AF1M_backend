package big.data.bigdata.service;

import big.data.bigdata.entity.GwasTask;
import big.data.bigdata.mapper.GwasMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class GwasService {
/*    @Autowired
    private GwasMapper gwasMapper;

    @Autowired
    private GwasNotificationService notificationService;

    @Value("${gwas.upload.dir:./gwas-files}")
    private String uploadDir;

    @Value("${gwas.result.dir:./gwas-results}")
    private String resultDir;

    *//**
     * 执行GWAS分析
     *//*
    public GwasTask runGwasAnalysis(MultipartFile file, String taskName, double threshold, String model) throws IOException, IOException {
        // 检查并创建目录
        Path uploadPath = Paths.get(uploadDir);
        Path resultPath = Paths.get(resultDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        if (!Files.exists(resultPath)) {
            Files.createDirectories(resultPath);
        }

        // 保存上传文件
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String savedFileName = taskName + fileExtension;
        Path filePath = uploadPath.resolve(savedFileName);
        Files.copy(file.getInputStream(), filePath);

        // 创建并保存任务信息
        GwasTask task = new GwasTask();
        task.setTaskName(taskName);
        task.setThreshold(threshold);
        task.setModel(model);
        task.setFileName(savedFileName);
        task.setStatus("Queued");
        task.setProgress(0.0);

        gwasMapper.insertTask(task);

        // 发送初始状态消息
        notificationService.sendProgressUpdate(task);

        // 异步执行分析任务
        CompletableFuture.runAsync(() -> {
            try {
                executeAnalysis(task, filePath.toString());
            } catch (Exception e) {
                log.error("GWAS analysis failed for task {}: {}", taskName, e.getMessage(), e);
                task.setStatus("Failed");
                task.setErrorMessage("Analysis failed: " + e.getMessage());
                gwasMapper.updateTask(task);
                notificationService.sendTaskFailed(task);
            }
        });

        return task;
    }

    *//**
     * 执行分析过程
     *//*
    private void executeAnalysis(GwasTask task, String filePath) throws Exception {
        // 更新任务状态
        task.setStatus("InProgress");
        gwasMapper.updateTask(task);
        notificationService.sendProgressUpdate(task);

        // 为本地测试，我们创建一个简单的脚本模拟分析过程
        String resultFilePath = Paths.get(resultDir, task.getTaskName() + "_results.csv").toString();
        boolean isSuccess = simulateGwasAnalysis(filePath, resultFilePath, task);

        if (isSuccess) {
            task.setStatus("Completed");
            task.setProgress(100.0);
            task.setResultFile(task.getTaskName() + "_results.csv");
            gwasMapper.updateTask(task);
            notificationService.sendTaskCompleted(task);
        } else {
            task.setStatus("Failed");
            task.setErrorMessage("Analysis failed during execution");
            gwasMapper.updateTask(task);
            notificationService.sendTaskFailed(task);
        }
    }

    *//**
     * 模拟GWAS分析过程（本地测试用）
     *//*
    private boolean simulateGwasAnalysis(String inputFile, String outputFile, GwasTask task) {
        try {
            // 模拟处理时间
            long totalSteps = 100;
            for (int i = 0; i < totalSteps; i++) {
                Thread.sleep(200); // 模拟处理延迟
                double progress = (double) i / totalSteps * 100.0;

                // 更新进度并通知
                task.setProgress(progress);
                gwasMapper.updateTask(task);

                if (i % 5 == 0) { // 每5%发送一次进度更新
                    notificationService.sendProgressUpdate(task);
                }
            }

            // 生成模拟结果文件
            generateMockResults(outputFile, task.getThreshold(), task.getModel());
            return true;
        } catch (Exception e) {
            log.error("Error in GWAS simulation", e);
            return false;
        }
    }

    *//**
     * 生成模拟的GWAS结果
     *//*
    private void generateMockResults(String outputFile, double threshold, String model) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            // 写入CSV头
            writer.println("snp_id,chromosome,position,p_value,effect_size,allele");

            // 生成随机结果数据
            for (int i = 1; i <= 1000; i++) {
                String snpId = "rs" + (10000000 + i);
                int chromosome = (int) (Math.random() * 22) + 1;
                int position = (int) (Math.random() * 10000000);
                double pValue = Math.random();
                double effectSize = -0.5 + Math.random();
                String allele = (Math.random() > 0.5) ? "A/G" : "C/T";

                writer.println(String.format("%s,%d,%d,%.8f,%.4f,%s",
                        snpId, chromosome, position, pValue, effectSize, allele));
            }
        }
    }

    *//**
     * 获取任务状态
     *//*
    public List<GwasTask> getTaskStatus(String taskName) {
        return gwasMapper.findByTaskName(taskName);
    }

    *//**
     * 获取所有任务
     *//*
    public List<GwasTask> getAllTasks() {
        return gwasMapper.findAllTasks();
    }

    *//**
     * 获取分析结果预览
     *//*
    public List<Map<String, Object>> getResultsPreview(String taskName) {
        List<GwasTask> tasks = gwasMapper.findByTaskName(taskName);
        if (tasks.isEmpty() || !"Completed".equals(tasks.get(0).getStatus())) {
            return new ArrayList<>();
        }

        String resultFileName = tasks.get(0).getResultFile();
        if (resultFileName == null) {
            return new ArrayList<>();
        }

        Path resultPath = Paths.get(resultDir, resultFileName);
        if (!Files.exists(resultPath)) {
            return new ArrayList<>();
        }

        try {
            List<Map<String, Object>> preview = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(resultPath.toFile()));

            // 读取表头
            String header = reader.readLine();
            String[] headers = header.split(",");

            // 读取前10行数据
            for (int i = 0; i < 10; i++) {
                String line = reader.readLine();
                if (line == null) break;

                String[] values = line.split(",");
                Map<String, Object> row = new HashMap<>();

                for (int j = 0; j < headers.length; j++) {
                    row.put(headers[j], j < values.length ? values[j] : "");
                }

                preview.add(row);
            }

            reader.close();
            return preview;
        } catch (Exception e) {
            log.error("Error reading result file", e);
            return new ArrayList<>();
        }
    }

    *//**
     * 获取分析结果文件Path
     *//*
    public Path getResultFilePath(String taskName) {
        List<GwasTask> tasks = gwasMapper.findByTaskName(taskName);
        if (tasks.isEmpty() || !"Completed".equals(tasks.get(0).getStatus()) || tasks.get(0).getResultFile() == null) {
            throw new RuntimeException("Result not found or task not completed");
        }

        Path resultPath = Paths.get(resultDir, tasks.get(0).getResultFile());
        if (!Files.exists(resultPath)) {
            throw new RuntimeException("Result file not found");
        }

        return resultPath;
    }*/
}
