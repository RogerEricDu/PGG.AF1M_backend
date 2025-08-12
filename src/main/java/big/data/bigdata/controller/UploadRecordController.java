package big.data.bigdata.controller;


import big.data.bigdata.dto.UploadRequest;
import big.data.bigdata.entity.UploadRecord;
import big.data.bigdata.service.UploadRecordService;
import big.data.bigdata.service.impl.UploadRecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/upload")
public class UploadRecordController {
    @Autowired
    private UploadRecordService uploadRecordService;

    @PostMapping("/excel")
    public ResponseEntity<Map<String, Object>> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                                @RequestParam("username") String username,
                                                                @RequestParam("taskName") String taskName,
                                                                @RequestParam("email") String email) {
        try {
            uploadRecordService.processExcel(file, username, email, taskName);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("msg", "Upload successful");
            Map<String, String> data = new HashMap<>();
            data.put("taskName", taskName);
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("msg", "Upload failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/tasks")
    public ResponseEntity<Map<String, Object>> getUserTasks(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        Map<String, Object> response = uploadRecordService.getUserTasks(username);
        return ResponseEntity.ok(response);
    }

/*    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        try {
            return uploadRecordService.downloadResultFile(id);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/
    @PostMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestBody Map<String, Long> request) {
        Long id = request.get("id");
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return uploadRecordService.downloadResultFile(id);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
