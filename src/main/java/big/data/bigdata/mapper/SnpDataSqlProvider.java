package big.data.bigdata.mapper;

import org.apache.ibatis.annotations.Param;


public class SnpDataSqlProvider {
    public String findSnpData(@Param("chromosome") int chromosome,
                              @Param("rsId") String rsId,
                              @Param("position") Integer position,
                              @Param("population")String population,
                              @Param("variant") String variant,
                              @Param("offset") int offset,
                              @Param("limit") int limit) {
        StringBuilder sql = new StringBuilder();

        //构建基础查询
        sql.append("SELECT * FROM snp_data_chr").append(chromosome).append(" WHERE 1=1");

        if (rsId != null ) {
            sql.append(" AND rs_id = #{rsId}");
        }
        if (position != null) {
            sql.append(" AND position = #{position}");
        }


        // 添加分页逻辑
        sql.append(" LIMIT #{limit} OFFSET #{offset}");

        return sql.toString();
    }

    // 查询总数
    public String findSnpDataCount(@Param("chromosome") int chromosome,
                                   @Param("rsId") String rsId,
                                   @Param("position") Integer position,
                                   @Param("population") String population,
                                   @Param("variant") String variant) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM snp_data_chr").append(chromosome).append(" WHERE 1=1");

        if (rsId != null) {
            sql.append(" AND rs_id = #{rsId}");
        }
        if (position != null) {
            sql.append(" AND position = #{position}");
        }
/*        if (population != null) {
            sql.append(" AND population = #{population}");
        }
        if (variant != null) {
            sql.append(" AND variant = #{variant}");
        }*/

        return sql.toString();
    }

    public String findSnpDataByTable(@Param("tableName") String tableName,
                                     @Param("dataset")String dataset,
                                     @Param("rsId") String rsId,
                                     @Param("position") Integer position,
                                     @Param("population")String population,
                                     @Param("variant") String variant,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(tableName).append(" WHERE 1=1");

        // 将空字符串转化为 null
        if (rsId != null && rsId.equals("")) rsId = null;
        if (dataset != null && dataset.equals("")) dataset = null;
        if (population != null && population.equals("")) population = null;
        if (variant != null && variant.equals("")) variant = null;

        if (rsId != null) {
            sql.append(" AND rs_id = #{rsId}");
        }
        if (dataset != null) {
            sql.append(" AND dataset = #{dataset}");
        }
        if (position != null) {
            sql.append(" AND position = #{position}");
        }
        if (population != null) {
            sql.append(" AND population = #{population}");
        }
        if (variant != null) {
            sql.append(" AND variant = #{variant}");
        }
        // 添加分页逻辑
        sql.append(" LIMIT #{limit} OFFSET #{offset}");

        return sql.toString();
    }

    // 查询总数（基于表名）
    public String findSnpDataCountByTable(@Param("tableName") String tableName,
                                          @Param("dataset") String dataset,
                                          @Param("rsId") String rsId,
                                          @Param("position") Integer position,
                                          @Param("population") String population,
                                          @Param("variant") String variant) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM ").append(tableName).append(" WHERE 1=1");

        // 将空字符串转化为 null
        if (rsId != null && rsId.equals("")) rsId = null;
        if (dataset != null && dataset.equals("")) dataset = null;
        if (population != null && population.equals("")) population = null;
        if (variant != null && variant.equals("")) variant = null;

        if (rsId != null) {
            sql.append(" AND rs_id = #{rsId}");
        }
        if (dataset != null) {
            sql.append(" AND dataset = #{dataset}");
        }
        if (position != null) {
            sql.append(" AND position = #{position}");
        }
        if (population != null) {
            sql.append(" AND population = #{population}");
        }
        if (variant != null) {
            sql.append(" AND variant = #{variant}");
        }

        return sql.toString();
    }

/*    public String findSnpDataByPopulation(@Param("chromosome") int chromosome,
                                          @Param("rsId") String rsId,
                                          @Param("position") Integer position,
                                          @Param("population")String population,
                                          @Param("variant") String variant,
                                          @Param("offset") int offset,
                                          @Param("limit") int limit) {
        StringBuilder sql = new StringBuilder();

        //构建基础查询
        sql.append("SELECT * FROM population_snp_data_chr").append(chromosome).append(" WHERE 1=1");

        if (rsId != null ) {
            sql.append(" AND rs_id = #{rsId}");
        }
        if (position != null) {
            sql.append(" AND position = #{position}");
        }

        // 添加分页逻辑
        sql.append(" LIMIT #{limit} OFFSET #{offset}");

        return sql.toString();
    }

    // 查询总数
    public String findSnpDataCountByPopulation(@Param("chromosome") int chromosome,
                                   @Param("rsId") String rsId,
                                   @Param("position") Integer position,
                                   @Param("population") String population,
                                   @Param("variant") String variant) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM population_snp_data_chr").append(chromosome).append(" WHERE 1=1");

        if (rsId != null) {
            sql.append(" AND rs_id = #{rsId}");
        }
        if (position != null ) {
            sql.append(" AND position = #{position}");
        }
        if (population != null && !population.isEmpty()) {
            sql.append(" AND population = #{population}");
        }
        if (variant != null && !variant.isEmpty()) {
            sql.append(" AND variant = #{variant}");
        }

        return sql.toString();
    }*/



    //前面是NGS的，后面都是芯片数据的接口
    public String findSnpDataMicroarray(@Param("chromosome") int chromosome,
                              @Param("rsId") String rsId,
                              @Param("position") Integer position,
                              @Param("population")String population,
                              @Param("variant") String variant,
                              @Param("offset") int offset,
                              @Param("limit") int limit) {
        StringBuilder sql = new StringBuilder();

        //构建基础查询
        sql.append("SELECT * FROM snp_data_chr").append(chromosome).append(" WHERE 1=1");

        if (rsId != null ) {
            sql.append(" AND rs_id = #{rsId}");
        }
        if (position != null) {
            sql.append(" AND position = #{position}");
        }
        if (population != null) {
            sql.append(" AND population = #{population}");
        }
        if (variant != null) {
            sql.append(" AND variant = #{variant}");
        }

        // 添加分页逻辑
        sql.append(" LIMIT #{limit} OFFSET #{offset}");

        return sql.toString();
    }

    // 查询总数
    public String findSnpDataCountMicroarray(@Param("chromosome") int chromosome,
                                   @Param("rsId") String rsId,
                                   @Param("position") Integer position,
                                   @Param("population") String population,
                                   @Param("variant") String variant) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM snp_data_chr").append(chromosome).append(" WHERE 1=1");

        if (rsId != null) {
            sql.append(" AND rs_id = #{rsId}");
        }
        if (position != null) {
            sql.append(" AND position = #{position}");
        }
        if (population != null) {
            sql.append(" AND population = #{population}");
        }
        if (variant != null) {
            sql.append(" AND variant = #{variant}");
        }

        return sql.toString();
    }

    public String findSnpDataByTableMicroarray(@Param("tableName") String tableName,
                                     @Param("dataset")String dataset,
                                     @Param("rsId") String rsId,
                                     @Param("position") Integer position,
                                     @Param("population")String population,
                                     @Param("variant") String variant,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(tableName).append(" WHERE 1=1");

        // 将空字符串转化为 null
        if (rsId != null && rsId.equals("")) rsId = null;
        if (dataset != null && dataset.equals("")) dataset = null;
        if (population != null && population.equals("")) population = null;
        if (variant != null && variant.equals("")) variant = null;

        if (rsId != null) {
            sql.append(" AND rs_id = #{rsId}");
        }
        if (dataset != null) {
            sql.append(" AND dataset = #{dataset}");
        }
        if (position != null) {
            sql.append(" AND position = #{position}");
        }
        if (population != null) {
            sql.append(" AND population = #{population}");
        }
        if (variant != null) {
            sql.append(" AND variant = #{variant}");
        }
        // 添加分页逻辑
        sql.append(" LIMIT #{limit} OFFSET #{offset}");

        return sql.toString();
    }

    // 查询总数（基于表名）
    public String findSnpDataCountByTableMicroarray(@Param("tableName") String tableName,
                                          @Param("dataset") String dataset,
                                          @Param("rsId") String rsId,
                                          @Param("position") Integer position,
                                          @Param("population") String population,
                                          @Param("variant") String variant) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM ").append(tableName).append(" WHERE 1=1");

        // 将空字符串转化为 null
        if (rsId != null && rsId.equals("")) rsId = null;
        if (dataset != null && dataset.equals("")) dataset = null;
        if (population != null && population.equals("")) population = null;
        if (variant != null && variant.equals("")) variant = null;

        if (rsId != null) {
            sql.append(" AND rs_id = #{rsId}");
        }
        if (dataset != null) {
            sql.append(" AND dataset = #{dataset}");
        }
        if (position != null) {
            sql.append(" AND position = #{position}");
        }
        if (population != null) {
            sql.append(" AND population = #{population}");
        }
        if (variant != null) {
            sql.append(" AND variant = #{variant}");
        }

        return sql.toString();
    }


}
