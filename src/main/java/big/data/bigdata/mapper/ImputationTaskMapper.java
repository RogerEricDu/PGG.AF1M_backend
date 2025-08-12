package big.data.bigdata.mapper;

import big.data.bigdata.entity.ImputationTask;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ImputationTaskMapper {
    @InsertProvider(type = ImputationTaskSqlProvider.class, method = "insertTask")
    void insertTask(ImputationTask task);

    @SelectProvider(type = ImputationTaskSqlProvider.class, method = "getTaskByUsername")
    List<ImputationTask> getTaskByUsername(@Param("username") String username);

    @SelectProvider(type = ImputationTaskSqlProvider.class, method = "getTaskByTaskName")
    ImputationTask getTaskByTaskName(@Param("taskName") String taskName);
}
