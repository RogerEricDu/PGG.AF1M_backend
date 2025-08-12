package big.data.bigdata.mapper;

import big.data.bigdata.entity.ProvincialPopulation;
import big.data.bigdata.entity.VariantEffect;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface ProvincialPopulationMapper {
    // 根据染色体编号和位置查询 ProvincialPopulation，并支持分页
    @SelectProvider(type = ProvincialPopulationSqlProvider.class, method = "selectProvincialPopulationByChromosomeAndPosition")
    @Results({
            @Result(property = "snpId", column = "snp_id"),
            @Result(property = "position", column = "position"),
            //@Result(property = "province", column = "province"),
            @Result(property = "alleleCount",column = "allele_count"),
            @Result(property = "refAllele", column = "ref_allele"),
            @Result(property = "altAllele", column = "alt_allele"),
            @Result(property = "refAlleleFrequency", column = "ref_allele_frequency"),
            @Result(property = "altAlleleFrequency", column = "alt_allele_frequency"),
            @Result(property = "genotype1", column = "genotype1"),
            @Result(property = "genotypeFrequency1", column = "genotype_frequency1"),
            @Result(property = "genotype2", column = "genotype2"),
            @Result(property = "genotypeFrequency2", column = "genotype_frequency2"),
            @Result(property = "genotype3", column = "genotype3"),
            @Result(property = "genotypeFrequency3", column = "genotype_frequency3"),
            @Result(property = "population", column = "population")
    })
    List<ProvincialPopulation> findProvincialPopulationByChromosomeAndPosition(
            @Param("tableName") String tableName,
            @Param("position") Integer position,
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("population") String population
    );

    // 查询总记录数
    @SelectProvider(type = ProvincialPopulationSqlProvider.class, method = "countProvincialPopulationByChromosomeAndPosition")
    long countProvincialPopulationByChromosomeAndPosition(
            @Param("tableName") String tableName,
            @Param("position") Integer position
    );
}