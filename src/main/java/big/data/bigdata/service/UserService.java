package big.data.bigdata.service;

import big.data.bigdata.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    /**
     * 获取所有用户
     */
    List<User> findAllUsers();

    /**
     * 通过ID查找用户
     * @param id 用户ID
     * @return Optional包装的用户对象
     */
    Optional<User> findUserById(Long id);

    /**
     * 添加新用户
     * @param user 用户对象
     * @return 保存后的用户对象
     */
    User addUser(User user);

    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 更新后的用户对象
     */
    User updateUser(User user);

    /**
     * 通过id删除用户
     * @param id 用户ID
     */
    void deleteUser(Long id);

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户对象
     */
    User findByUsername(String username);
}
