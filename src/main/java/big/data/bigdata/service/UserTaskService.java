package big.data.bigdata.service;

import big.data.bigdata.dto.UserInfoDTO;
import big.data.bigdata.entity.User;
import big.data.bigdata.entity.UserTask;
import big.data.bigdata.mapper.UserTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserTaskService {
    private final UserTaskMapper userTaskMapper;

    @Autowired
    public UserTaskService(UserTaskMapper userTaskMapper){
        this.userTaskMapper = userTaskMapper;
    }

    public Map<String, Object> getUserTasks(String username, String email){
        List<UserTask> tasks = userTaskMapper.findAllUserTasks(username, email);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "success");
        result.put("data", tasks);

        return result;
    }

/*    public User findUserInfo(String username){
        return userTaskMapper.findUserByUsername(username);
    }*/
}
