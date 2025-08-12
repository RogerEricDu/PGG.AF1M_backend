package big.data.bigdata.service;

import big.data.bigdata.entity.GwasTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class GwasNotificationService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    //发送任务进度更新
    public void sendProgressUpdate(GwasTask task) {
        messagingTemplate.convertAndSend("/topic/gwas/" + task.getTaskName(), task);
    }

    //发送任务完成通知
    public void sendTaskCompleted(GwasTask task) {
        messagingTemplate.convertAndSend("/topic/gwas/completed", task);
    }

    //发送任务失败通知
    public void sendTaskFailed(GwasTask task) {
        messagingTemplate.convertAndSend("/topic/gwas/failed", task);
    }
}
