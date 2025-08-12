package big.data.bigdata.mapper;

import big.data.bigdata.entity.GlobalSnpData;
import big.data.bigdata.entity.ProvincialPopulation;
import big.data.bigdata.entity.SnpData;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface GlobalMapMapper {
    // 根据染色体编号和位置查询 ProvincialPopulation，并支持分页
    @SelectProvider(type = GlobalMapSqlProvider.class, method = "findGlobalSnpData")
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
            @Result(property = "alleleCount",column = "allele_count"),
            @Result(property = "country",column = "country")
    })
    List<GlobalSnpData> findGlobalSnpData(@Param("chromosome") int chromosome,
                                          @Param("position") Integer position,
                                          @Param("country") String country);

/*    // 查询符合条件的记录总数
    @SelectProvider(type = SnpDataSqlProvider.class, method = "findGlobalSnpDataCount")
    int findGlobalSnpDataCount(@Param("chromosome") int chromosome,
                         @Param("position") Integer position,
                         @Param("country") String country);*/
}
