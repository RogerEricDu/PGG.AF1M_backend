package big.data.bigdata.controller;

import big.data.bigdata.dto.UserInfoDTO;
import big.data.bigdata.dto.UserTaskDTO;
import big.data.bigdata.entity.User;
import big.data.bigdata.entity.UserTask;
import big.data.bigdata.service.UserTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/profile")

public class UserTaskController {
    private final UserTaskService userTaskService;

    @Autowired
    public UserTaskController(UserTaskService userTaskService){
        this.userTaskService = userTaskService;
    }

    @PostMapping("/tasks")
    public Object getUserTasks(@RequestBody UserTaskDTO dto){
        return userTaskService.getUserTasks(dto.getUsername(), dto.getEmail());
    }

/*    @PostMapping("/userInfo")
    public ResponseEntity<?> getUserInfo(@RequestBody UserInfoDTO dto) {
        User user = userTaskService.findUserInfo(dto.getUsername());
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }*/
}
