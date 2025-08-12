package big.data.bigdata.mapper;

import big.data.bigdata.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface UserMapper{
    // 查询所有用户
    @Select("SELECT * FROM user")
    List<User> selectList();

    // 通过ID查询用户
    @Select("SELECT * FROM user WHERE id = #{id}")
    User selectById(Long id);

    //根据用户名查找用户
    @Select("SELECT * FROM user WHERE username = #{username}")
    User selectByUsername(String username);

    // 插入新用户
    @Insert("INSERT INTO user(username, password, email, organization, department, phone_number) VALUES(#{username}, #{password}, #{email}, #{organization}, #{department}, #{phone_number})")
    @Options(useGeneratedKeys = true, keyProperty = "id")  // 使用自增主键
    void insert(User user);

    // 更新用户信息
    @Update("UPDATE user SET username = #{username}, password = #{password}, email = #{email}, organization = #{organization}, department = #{department}, phone_number = #{phone_number} WHERE username = #{username}")
    int updateById(User user);

    // 根据ID删除用户
    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteById(Long id);

}
