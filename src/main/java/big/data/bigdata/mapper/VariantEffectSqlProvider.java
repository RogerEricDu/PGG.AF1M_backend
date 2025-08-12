package big.data.bigdata.mapper;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class VariantEffectSqlProvider {

    // 查询分页数据
    public String selectVariantEffectsByChromosomeAndPosition(final Map<String, Object> params) {
        int chromosome = (int) params.get("chromosome");
        String tableName = "vep_snp_data_chr" + chromosome;

        return new SQL() {{
            SELECT("*");
            FROM(tableName);
            WHERE("position = #{position}");
            // 不能在这里直接拼 offset/limit，因为 MyBatis 不支持 limit 动态参数拼接
        }}.toString() + " LIMIT #{limit} OFFSET #{offset}";
    }

    // 查询总数
    public String countVariantEffectsByChromosomeAndPosition(final Map<String, Object> params) {
        int chromosome = (int) params.get("chromosome");
        String tableName = "vep_snp_data_chr" + chromosome;

        return new SQL() {{
            SELECT("COUNT(*)");
            FROM(tableName);
            WHERE("position = #{position}");
        }}.toString();
    }
}

