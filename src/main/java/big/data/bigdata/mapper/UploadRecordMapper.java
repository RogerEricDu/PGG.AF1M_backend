package big.data.bigdata.mapper;

import big.data.bigdata.entity.UploadRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface UploadRecordMapper {
    @Insert("""
        INSERT INTO upload_record (task_name, file_info, data_type, status, username, email, result_path, created_at)
        VALUES (#{taskName}, #{fileInfo}, #{dataType}, #{status}, #{username}, #{email}, #{resultPath}, NOW())
    """)
    void insert(UploadRecord record);

    @Select("SELECT id, task_name, data_type, result_path, status, username, email, created_at FROM upload_record WHERE username = #{username} ORDER BY created_at DESC")
    List<Map<String, Object>> findByUsername(@Param("username") String username);

    @Select("""
        SELECT 
          id,
          task_name AS taskName,
          file_info AS fileInfo,
          data_type AS dataType,
          status,
          username,
          email,
          result_path AS resultPath,
          created_at AS createdAt
        FROM upload_record
        WHERE id = #{id}
    """)
    UploadRecord findById(Long id);


    //定时检测
    @Select("""
    SELECT * FROM upload_record WHERE created_at < #{cutoff}
""")
    List<UploadRecord> findOlderThan(@Param("cutoff") LocalDateTime cutoff);

    @Delete("DELETE FROM upload_record WHERE id = #{id}")
    void deleteById(Long id);
}