package big.data.bigdata.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class ProvincialPopulationSqlProvider {
    //动态生成查询SQL
    public String selectProvincialPopulationByChromosomeAndPosition(@Param("tableName") String tableName,
                                                                    @Param("position") Integer position,
                                                                    @Param("offset") int offset,
                                                                    @Param("limit") int limit,
                                                                    @Param("population") String population){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(tableName).append(" WHERE position = #{position}");
        // 添加族群和位置的过滤条件
        if (position > 0) {
            sql.append(" AND position = ").append(position);
        }
        if (population != null && !population.isEmpty()) {
            sql.append(" AND population = '").append(population).append("'");
        }
        // 添加分页逻辑
        if (limit > 0) {
            sql.append(" LIMIT ").append(limit);
        }
        if (offset > 0) {
            sql.append(" OFFSET ").append(offset);
        }

        return sql.toString();
    }
    // 查询总数（基于表名）
    public String countProvincialPopulationByChromosomeAndPosition(@Param("tableName") String tableName,
                                                                   @Param("position") Integer position){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM ").append(tableName).append(" WHERE position = #{position}");
        return sql.toString();
    }
}
