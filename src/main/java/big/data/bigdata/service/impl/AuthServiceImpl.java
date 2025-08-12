package big.data.bigdata.service.impl;

import big.data.bigdata.entity.User;
import big.data.bigdata.mapper.UserMapper;
import big.data.bigdata.service.AuthService;
import big.data.bigdata.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthServiceImpl(UserMapper userMapper, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String authenticate(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user != null && password.equals(user.getPassword())) {
            return jwtUtil.generateToken(user);
        }
        throw new IllegalArgumentException("Invalid username or password");
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
}
