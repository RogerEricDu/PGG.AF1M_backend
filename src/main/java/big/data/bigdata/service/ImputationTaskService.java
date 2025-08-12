package big.data.bigdata.service;

import big.data.bigdata.entity.ImputationTask;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImputationTaskService {
    void createTask(MultipartFile file, String username, String email, String taskName, String panelType);
    List<ImputationTask> getUserTasks(String username);
    Resource downloadOutput(String taskName);
}

