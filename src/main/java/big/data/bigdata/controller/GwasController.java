package big.data.bigdata.controller;

import big.data.bigdata.entity.GwasTask;
import big.data.bigdata.service.GwasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gwas")
public class GwasController {

/*    @Autowired
    private GwasService gwasService;

    //运行GWAS分析
    @PostMapping("/run")
    public ResponseEntity<?> runGwasAnalysis(
            @RequestParam("file") MultipartFile file,
            @RequestParam("taskName") String taskName,
            @RequestParam("threshold") double threshold,
            @RequestParam("model") String model){
        try{
            GwasTask task = gwasService.runGwasAnalysis(file, taskName, threshold, model);

            // 返回初始预览（空数据）
            Map<String, Object> response = new HashMap<>();
            response.put("taskId", task.getId());
            response.put("status", task.getStatus());
            response.put("preview", List.of());

            return ResponseEntity.ok(response);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to run GWAS analysis: " + e.getMessage());
        }
    }

    //获取任务状态
    @GetMapping("/status/{taskName}")
    public ResponseEntity<?> getTaskStatus(@PathVariable String taskName) {
        try {
            List<GwasTask> tasks = gwasService.getTaskStatus(taskName);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get task status: " + e.getMessage());
        }
    }

    //获取所有任务
    @GetMapping("/tasks")
    public ResponseEntity<?> getAllTasks() {
        try {
            List<GwasTask> tasks = gwasService.getAllTasks();
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get tasks: " + e.getMessage());
        }
    }

    //获取结果预览
    @GetMapping("/preview/{taskName}")
    public ResponseEntity<?> getResultsPreview(@PathVariable String taskName) {
        try {
            List<Map<String, Object>> preview = gwasService.getResultsPreview(taskName);
            Map<String, Object> response = new HashMap<>();
            response.put("preview", preview);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get results preview: " + e.getMessage());
        }
    }

    //下载结果文件
    @GetMapping("/download/{taskName}")
    public ResponseEntity<Resource> downloadResults(@PathVariable String taskName) {
        try {
            Path resultPath = gwasService.getResultFilePath(taskName);
            Resource resource = new FileSystemResource(resultPath);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resultPath.getFileName().toString() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }*/
}
