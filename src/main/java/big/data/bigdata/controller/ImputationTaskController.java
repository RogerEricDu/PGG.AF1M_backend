package big.data.bigdata.controller;

import big.data.bigdata.entity.ImputationTask;
import big.data.bigdata.service.ImputationTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/imputation")
public class ImputationTaskController {

    @Autowired
    private ImputationTaskService taskService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file,
                                    @RequestParam("username") String username,
                                    @RequestParam("email") String email,
                                    @RequestParam("taskName") String taskName,
                                    @RequestParam("panelType") String panelType) {
        taskService.createTask(file, username, email, taskName, panelType);
        return ResponseEntity.ok("任务已提交");
    }

    @GetMapping("/status")
    public ResponseEntity<List<ImputationTask>> getStatus(@RequestParam("username") String username) {
        return ResponseEntity.ok(taskService.getUserTasks(username));
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("taskName") String taskName) {
        Resource file = taskService.downloadOutput(taskName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFilename())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }
}

