package big.data.bigdata.service;

import big.data.bigdata.entity.User;

public interface AuthService {
    String authenticate(String username, String password) throws IllegalArgumentException;
    User getUserByUsername(String username); // 可根据实际需求决定是否暴露
}
