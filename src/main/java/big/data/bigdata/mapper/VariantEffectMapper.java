package big.data.bigdata.mapper;

import big.data.bigdata.entity.VariantEffect;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface VariantEffectMapper {

    // 分页查数据
    @SelectProvider(type = VariantEffectSqlProvider.class, method = "selectVariantEffectsByChromosomeAndPosition")
    @Results({
            @Result(property = "snpId", column = "snp_id"),
            @Result(property = "position", column = "position"),
            @Result(property = "refAllele", column = "ref_allele"),
            @Result(property = "altAllele", column = "alt_allele"),
            @Result(property = "symbol", column = "symbol"),
            @Result(property = "gene", column = "gene"),
            @Result(property = "exon", column = "exon"),
            @Result(property = "cDNAPosition", column = "cDNA_position"),
            @Result(property = "biotype", column = "biotype"),
            @Result(property = "consequence", column = "consequence"),
            @Result(property = "feature", column = "feature"),
            @Result(property = "featureType", column = "feature_type"),
            @Result(property = "featureStrand", column = "feature_strand"),
    })
    List<VariantEffect> findVariantEffectsByChromosomeAndPosition(
            @Param("chromosome") int chromosome,
            @Param("position") int position,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    // 查总数
    @SelectProvider(type = VariantEffectSqlProvider.class, method = "countVariantEffectsByChromosomeAndPosition")
    long countVariantEffectsByChromosomeAndPosition(
            @Param("chromosome") int chromosome,
            @Param("position") int position
    );
}

