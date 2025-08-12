package big.data.bigdata.service.impl;

import big.data.bigdata.entity.ImputationTask;
import big.data.bigdata.mapper.ImputationTaskMapper;
import big.data.bigdata.service.ImputationTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ImputationTaskServiceImpl implements ImputationTaskService {

    @Autowired
    private ImputationTaskMapper mapper;

    private static final String BASE_UPLOAD_DIR = "/data/imputation/uploads/";
    private static final String BASE_OUTPUT_DIR = "/data/imputation/results/";

    @Override
    public void createTask(MultipartFile file, String username, String email, String taskName, String panelType) {
        try {
            // 保存文件
            File uploadDir = new File(BASE_UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String filename = file.getOriginalFilename();
            String inputPath = BASE_UPLOAD_DIR + UUID.randomUUID() + "_" + filename;
            file.transferTo(new File(inputPath));

            // 创建任务对象
            ImputationTask task = new ImputationTask();
            task.setUsername(username);
            task.setEmail(email);
            task.setFilename(filename);
            task.setTaskName(taskName);
            task.setPanelType(panelType);
            task.setInputPath(inputPath);
            task.setOutputPath(BASE_OUTPUT_DIR + taskName); // 目录
            task.setStatus("running");

            mapper.insertTask(task);

            // 异步执行脚本
            new Thread(() -> runImputationScript(task)).start();

        } catch (IOException e) {
            throw new RuntimeException("上传失败: " + e.getMessage());
        }
    }

    private void runImputationScript(ImputationTask task) {
        try {
            String panelPath = getPanelPath(task.getPanelType());
            String script = String.format(
                    "bash run_imputation.sh %s %s %s",
                    task.getInputPath(),
                    panelPath,
                    task.getOutputPath()
            );
            Process proc = Runtime.getRuntime().exec(script);
            proc.waitFor();

            task.setStatus("done");
        } catch (Exception e) {
            task.setStatus("failed");
        } finally {
            task.setUpdatedAt(LocalDateTime.now());
            mapper.insertTask(task); // 你可以改为 updateTask
        }
    }

    private String getPanelPath(String panelType) {
        switch (panelType) {
            case "10k":
                return "/home/zhangxiaoxi/PGG.Han.2.0/total/101.total.chr*.m3vcf.gz";
            case "20k":
                return "/home/sunyumeng/data/panel/v2.20k.chr*.msav";
            case "100k":
                return "/share1/home/.../ArrayData48.chr*.vcf.gz";
            default:
                throw new IllegalArgumentException("未知 panel 类型: " + panelType);
        }
    }

    @Override
    public List<ImputationTask> getUserTasks(String username) {
        return mapper.getTaskByUsername(username);
    }

    @Override
    public Resource downloadOutput(String taskName) {
        ImputationTask task = mapper.getTaskByTaskName(taskName);
        if (task == null || !"done".equals(task.getStatus())) {
            throw new RuntimeException("任务未完成或不存在");
        }
        FileSystemResource file = new FileSystemResource(task.getOutputPath() + "/output.vcf.gz");
        if (!file.exists()) throw new RuntimeException("文件不存在");
        return file;
    }
}
