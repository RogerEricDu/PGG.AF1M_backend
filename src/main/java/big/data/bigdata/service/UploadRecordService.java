package big.data.bigdata.service;

import big.data.bigdata.entity.UploadRecord;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UploadRecordService {
    void processExcel(MultipartFile file, String username, String email, String taskName) throws Exception;
    Map<String, Object> getUserTasks(String username);
    ResponseEntity<Resource> downloadResultFile(Long recordId) throws IOException;
}
