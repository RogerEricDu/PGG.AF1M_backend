package big.data.bigdata.controller;


import big.data.bigdata.entity.User;
import big.data.bigdata.service.AuthService;
import big.data.bigdata.service.UserService;
import big.data.bigdata.utils.JwtUtil;
import big.data.bigdata.utils.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtUtil jwtUtil;

    // 用户注册
    @PostMapping("/register")
    public Response<?> registerUser(@RequestBody User user) {
        userService.addUser(user);
        return Response.success("User Registered successfully", null);
    }

    // 用户登录
    @PostMapping("/login")
    public Response<?> loginUser(@RequestBody User loginDetails) {
        try {
            String token = authService.authenticate(loginDetails.getUsername(), loginDetails.getPassword());
            return Response.success("Login successful", token);
        } catch (IllegalArgumentException e) {
            return Response.error(401, "Invalid Username/Password");
        }
    }

    // 获取当前用户信息（从token解析）
    @GetMapping("/me")
    public Response<?> getCurrentUserInfo(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.error(401, "Missing or invalid Authorization header");
        }

        String token = authHeader.replace("Bearer ", "");
        String username;
        try {
            username = jwtUtil.extractUsername(token);
        } catch (Exception e) {
            return Response.error(401, "Invalid token");
        }

        User user = userService.findByUsername(username);
        if (user == null) {
            return Response.error(404, "User not found");
        }

        return Response.success(user);
    }

    // 更新用户信息
    @PutMapping("")
    public Response<?> updateUserInfo(@RequestBody User userUpdates) {
        userService.updateUser(userUpdates);
        return Response.success("Update successful", null);
    }

    // 获取所有用户
    @GetMapping()
    public Response<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return Response.success(users);
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public Response<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Response.success("User deleted successfully", null);
    }
}
