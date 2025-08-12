package big.data.bigdata.dto;

import java.util.List;
import java.util.Map;

public class UploadRequest {
    private String taskName;
    private String dataType;
    private String username;
    private String email;
    private List<Map<String, Object>> snpList;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Map<String, Object>> getSnpList() {
        return snpList;
    }

    public void setSnpList(List<Map<String, Object>> snpList) {
        this.snpList = snpList;
    }
}
