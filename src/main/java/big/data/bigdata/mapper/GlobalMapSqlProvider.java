package big.data.bigdata.mapper;

import org.apache.ibatis.annotations.Param;

public class GlobalMapSqlProvider {
    public String findGlobalSnpData(@Param("chromosome") int chromosome,
                              @Param("position") Integer position,
                              @Param("country")String country) {
        StringBuilder sql = new StringBuilder();

        //构建基础查询
        sql.append("SELECT * FROM global_snp_data_chr").append(chromosome).append(" WHERE 1=1");

        if (country != null ) {
            sql.append(" AND country = #{country}");
        }
        if (position != null) {
            sql.append(" AND position = #{position}");
        }

        return sql.toString();
    }

/*    // 查询总数
    public String findGlobalSnpDataCount(@Param("chromosome") int chromosome,
                                   @Param("position") Integer position,
                                   @Param("country") String country) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM global_snp_data_chr").append(chromosome).append(" WHERE 1=1");

        if (country != null) {
            sql.append(" AND country = #{country}");
        }
        if (position != null) {
            sql.append(" AND position = #{position}");
        }

        return sql.toString();
    }*/
}
