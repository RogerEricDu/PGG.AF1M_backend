package big.data.bigdata.mapper;

import big.data.bigdata.entity.ImputationTask;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class ImputationTaskSqlProvider {
    public String insertTask(ImputationTask task) {
        return new SQL() {{
            INSERT_INTO("imputation_task");
            VALUES("username", "#{username}");
            VALUES("email", "#{email}");
            VALUES("file_info", "#{filename}");
            VALUES("task_name", "#{taskName}");
            VALUES("panel_type", "#{panelType}");
            VALUES("status", "#{status}");
            VALUES("input_path", "#{inputPath}");
            VALUES("output_path", "#{outputPath}");
        }}.toString();
    }

    public String getTaskByUsername(Map<String, Object> params) {
        return new SQL() {{
            SELECT("*");
            FROM("imputation_task");
            WHERE("username = #{username}");
            ORDER_BY("created_at DESC");
        }}.toString();
    }

    public String getTaskByTaskName(Map<String, Object> params) {
        return new SQL() {{
            SELECT("*");
            FROM("imputation_task");
            WHERE("task_name = #{taskName}");
        }}.toString();
    }
}

