package big.data.bigdata.mapper;

import big.data.bigdata.entity.User;
import big.data.bigdata.entity.UserTask;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserTaskMapper {
    @Select("""
        SELECT id, task_name, 'gwas' AS task_type, status, file_info, created_at AS update_time
        FROM gwas_task
        WHERE username = #{username} AND email = #{email}
        UNION ALL
        SELECT id, task_name, 'imputation' AS task_type, status, file_info, created_at AS update_time
        FROM imputation_task
        WHERE username = #{username} AND email = #{email}
        UNION ALL
        SELECT id, task_name, 'upload' AS task_type, status, file_info AS file_name, created_at AS update_time
        FROM upload_record
        WHERE username = #{username} AND email = #{email}
        ORDER BY update_time DESC
    """)
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "task_name", property = "taskName"),
            @Result(column = "task_type", property = "taskType"),
            @Result(column = "status", property = "status"),
            @Result(column = "file_name", property = "fileName"),
            @Result(column = "update_time", property = "updateTime")
    })
    List<UserTask> findAllUserTasks(@Param("username")String username, @Param("email")String email);

/*    @Select("""
    SELECT username,email FROM user WHERE username = #{username}
""")
    @Results({
            @Result(column = "username", property = "username"),
            @Result(column = "email", property = "email"),
    })
    User findUserByUsername(@Param("username") String username);*/
}
