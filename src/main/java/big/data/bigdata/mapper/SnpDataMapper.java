package big.data.bigdata.mapper;


import big.data.bigdata.entity.SnpData;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;

import java.util.List;

public interface SnpDataMapper {
    // 查询分页数据，带 @Results 注解
    @SelectProvider(type = SnpDataSqlProvider.class, method = "findSnpData")
    //@Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000) // 启用流式查询
    @Results({
            @Result(property = "snpId", column = "snp_id"),
            @Result(property = "rsId", column = "rs_id"),
            @Result(property = "position", column = "position"),
            @Result(property = "refAllele", column = "ref_allele"),
            @Result(property = "altAllele", column = "alt_allele"),
            @Result(property = "refAlleleFrequency", column = "ref_allele_frequency"),
            @Result(property = "altAlleleFrequency", column = "alt_allele_frequency"),
            @Result(property = "dataset", column = "dataset"),
            @Result(property = "sampleSize", column = "sample_size"),
            @Result(property = "genotype1", column = "genotype1"),
            @Result(property = "genotypeFrequency1", column = "genotype_frequency1"),
            @Result(property = "genotype2", column = "genotype2"),
            @Result(property = "genotypeFrequency2", column = "genotype_frequency2"),
            @Result(property = "genotype3", column = "genotype3"),
            @Result(property = "genotypeFrequency3", column = "genotype_frequency3"),
            @Result(property = "variant", column = "variant"),
            @Result(property = "population", column = "population"),
            @Result(property = "alleleCount",column = "allele_count")
    })
    List<SnpData> findSnpData(@Param("chromosome") int chromosome,
                              @Param("rsId") String rsId,
                              @Param("position") Integer position,
                              @Param("population") String population,
                              @Param("variant") String variant,
                              @Param("offset") int offset,
                              @Param("limit") int limit);

    // 查询符合条件的记录总数
    @SelectProvider(type = SnpDataSqlProvider.class, method = "findSnpDataCount")
    int findSnpDataCount(@Param("chromosome") int chromosome,
                         @Param("rsId") String rsId,
                         @Param("position") Integer position,
                         @Param("population") String population,
                         @Param("variant") String variant);




    // 查询分页数据（基于表名）
    @SelectProvider(type = SnpDataSqlProvider.class, method = "findSnpDataByTable")
    //@Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000) // 启用流式查询
    @Results({
            @Result(property = "snpId", column = "snp_id"),
            @Result(property = "rsId", column = "rs_id"),
            @Result(property = "position", column = "position"),
            @Result(property = "refAllele", column = "ref_allele"),
            @Result(property = "altAllele", column = "alt_allele"),
            @Result(property = "refAlleleFrequency", column = "ref_allele_frequency"),
            @Result(property = "altAlleleFrequency", column = "alt_allele_frequency"),
            @Result(property = "dataset", column = "dataset"),
            @Result(property = "sampleSize", column = "sample_size"),
            @Result(property = "genotype1", column = "genotype1"),
            @Result(property = "genotypeFrequency1", column = "genotype_frequency1"),
            @Result(property = "genotype2", column = "genotype2"),
            @Result(property = "genotypeFrequency2", column = "genotype_frequency2"),
            @Result(property = "genotype3", column = "genotype3"),
            @Result(property = "genotypeFrequency3", column = "genotype_frequency3"),
            @Result(property = "variant", column = "variant"),
            @Result(property = "population", column = "population"),
            @Result(property = "alleleCount",column = "allele_count")
    })
    List<SnpData> findSnpDataByTable(@Param("tableName") String tableName,
                                     @Param("dataset") String dataset,
                                     @Param("rsId") String rsId,
                                     @Param("position") Integer position,
                                     @Param("population") String population,
                                     @Param("variant") String variant,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit);

    // 查询符合条件的记录总数
    // 根据表名查询符合条件的记录总数（基于表名）
    @SelectProvider(type = SnpDataSqlProvider.class, method = "findSnpDataCountByTable")
    int findSnpDataCountByTable(@Param("tableName") String tableName,
                                @Param("dataset") String dataset,
                                @Param("rsId") String rsId,
                                @Param("position") Integer position,
                                @Param("population") String population,
                                @Param("variant") String variant);


/*    @SelectProvider(type = SnpDataSqlProvider.class, method = "findSnpDataByPopulation")
    //@Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000) // 启用流式查询
    @Results({
            @Result(property = "snpId", column = "snp_id"),
            @Result(property = "rsId", column = "rs_id"),
            @Result(property = "position", column = "position"),
            @Result(property = "refAllele", column = "ref_allele"),
            @Result(property = "altAllele", column = "alt_allele"),
            @Result(property = "refAlleleFrequency", column = "ref_allele_frequency"),
            @Result(property = "altAlleleFrequency", column = "alt_allele_frequency"),
            @Result(property = "population", column = "population"),
            @Result(property = "dataset", column = "dataset"),
            @Result(property = "sampleSize", column = "sample_size"),
            @Result(property = "genotype1", column = "genotype1"),
            @Result(property = "genotypeFrequency1", column = "genotype_frequency1"),
            @Result(property = "genotype2", column = "genotype2"),
            @Result(property = "genotypeFrequency2", column = "genotype_frequency2"),
            @Result(property = "genotype3", column = "genotype3"),
            @Result(property = "genotypeFrequency3", column = "genotype_frequency3"),
            @Result(property = "variant", column = "variant"),
            @Result(property = "population", column = "population")
    })
    List<SnpData> findSnpDataByPopulation(@Param("chromosome") int chromosome,
                                          @Param("rsId") String rsId,
                                          @Param("position") Integer position,
                                          @Param("population") String population,
                                          @Param("variant") String variant,
                                          @Param("offset") int offset,
                                          @Param("limit") int limit);


    // 查询符合条件的记录总数
    @SelectProvider(type = SnpDataSqlProvider.class, method = "findSnpDataCountByPopulation")
    int findSnpDataCountByPopulation(@Param("chromosome") int chromosome,
                                     @Param("rsId") String rsId,
                                     @Param("position") Integer position,
                                     @Param("population") String population,
                                     @Param("variant") String variant);*/


    //前面都是NGS的，从这里开始是芯片数据的接口

    // 查询分页数据，带 @Results 注解
    @SelectProvider(type = SnpDataSqlProvider.class, method = "findSnpDataMicroarray")
    //@Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000) // 启用流式查询
    @Results({
            @Result(property = "snpId", column = "snp_id"),
            @Result(property = "rsId", column = "rs_id"),
            @Result(property = "position", column = "position"),
            @Result(property = "refAllele", column = "ref_allele"),
            @Result(property = "altAllele", column = "alt_allele"),
            @Result(property = "refAlleleFrequency", column = "ref_allele_frequency"),
            @Result(property = "altAlleleFrequency", column = "alt_allele_frequency"),
            @Result(property = "dataset", column = "dataset"),
            @Result(property = "sampleSize", column = "sample_size"),
            @Result(property = "genotype1", column = "genotype1"),
            @Result(property = "genotypeFrequency1", column = "genotype_frequency1"),
            @Result(property = "genotype2", column = "genotype2"),
            @Result(property = "genotypeFrequency2", column = "genotype_frequency2"),
            @Result(property = "genotype3", column = "genotype3"),
            @Result(property = "genotypeFrequency3", column = "genotype_frequency3"),
            @Result(property = "variant", column = "variant"),
            @Result(property = "population", column = "population"),
            @Result(property = "alleleCount",column = "allele_count")
    })
    List<SnpData> findSnpDataMicroarray(@Param("chromosome") int chromosome,
                              @Param("rsId") String rsId,
                              @Param("position") Integer position,
                              @Param("population") String population,
                              @Param("variant") String variant,
                              @Param("offset") int offset,
                              @Param("limit") int limit);

    // 查询符合条件的记录总数
    @SelectProvider(type = SnpDataSqlProvider.class, method = "findSnpDataCountMicroarray")
    int findSnpDataCountMicroarray(@Param("chromosome") int chromosome,
                         @Param("rsId") String rsId,
                         @Param("position") Integer position,
                         @Param("population") String population,
                         @Param("variant") String variant);




    // 查询分页数据（基于表名）
    @SelectProvider(type = SnpDataSqlProvider.class, method = "findSnpDataByTableMicroarray")
    //@Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000) // 启用流式查询
    @Results({
            @Result(property = "snpId", column = "snp_id"),
            @Result(property = "rsId", column = "rs_id"),
            @Result(property = "position", column = "position"),
            @Result(property = "refAllele", column = "ref_allele"),
            @Result(property = "altAllele", column = "alt_allele"),
            @Result(property = "refAlleleFrequency", column = "ref_allele_frequency"),
            @Result(property = "altAlleleFrequency", column = "alt_allele_frequency"),
            @Result(property = "dataset", column = "dataset"),
            @Result(property = "sampleSize", column = "sample_size"),
            @Result(property = "genotype1", column = "genotype1"),
            @Result(property = "genotypeFrequency1", column = "genotype_frequency1"),
            @Result(property = "genotype2", column = "genotype2"),
            @Result(property = "genotypeFrequency2", column = "genotype_frequency2"),
            @Result(property = "genotype3", column = "genotype3"),
            @Result(property = "genotypeFrequency3", column = "genotype_frequency3"),
            @Result(property = "variant", column = "variant"),
            @Result(property = "population", column = "population"),
            @Result(property = "alleleCount",column = "allele_count")
    })
    List<SnpData> findSnpDataByTableMicroarray(@Param("tableName") String tableName,
                                     @Param("dataset") String dataset,
                                     @Param("rsId") String rsId,
                                     @Param("position") Integer position,
                                     @Param("population") String population,
                                     @Param("variant") String variant,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit);

    // 查询符合条件的记录总数
    // 根据表名查询符合条件的记录总数（基于表名）
    @SelectProvider(type = SnpDataSqlProvider.class, method = "findSnpDataCountByTableMicroarray")
    int findSnpDataCountByTableMicroarray(@Param("tableName") String tableName,
                                @Param("dataset") String dataset,
                                @Param("rsId") String rsId,
                                @Param("position") Integer position,
                                @Param("population") String population,
                                @Param("variant") String variant);


}
