package big.data.bigdata.mapper;


import big.data.bigdata.entity.GwasTask;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GwasMapper {
    @Insert("INSERT INTO gwas_task (task_name, threshold, model, file_name, status, progress) " +
            "VALUES (#{taskName}, #{threshold}, #{model}, #{fileName}, #{status}, #{progress})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertTask(GwasTask task);

    @Update("UPDATE gwas_task SET status = #{status}, progress = #{progress}, " +
            "error_message = #{errorMessage}, result_file = #{resultFile} " +
            "WHERE id = #{id}")
    int updateTask(GwasTask task);

    @Select("SELECT * FROM gwas_task WHERE task_name = #{taskName}")
    List<GwasTask> findByTaskName(String taskName);

    @Select("SELECT * FROM gwas_task WHERE id = #{id}")
    GwasTask findById(Long id);

    @Select("SELECT * FROM gwas_task ORDER BY create_time DESC")
    List<GwasTask> findAllTasks();
}
