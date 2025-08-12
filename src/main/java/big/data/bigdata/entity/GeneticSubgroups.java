package big.data.bigdata.entity;

public class GeneticSubgroups {
    private Long snpId;
    private Integer position;
    private int alleleCount;
    private String dataset;
    private String refAllele;
    private String altAllele;
    private Float refAlleleFrequency;
    private Float altAlleleFrequency;
    private String genotype1;
    private Float genotypeFrequency1;
    private String genotype2;
    private Float genotypeFrequency2;
    private String genotype3;
    private Float genotypeFrequency3;
    private String population;

    public Long getSnpId() {
        return snpId;
    }

    public void setSnpId(Long snpId) {
        this.snpId = snpId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public int getAlleleCount() {return alleleCount;}

    public void setAlleleCount(int alleleCount) {this.alleleCount = alleleCount;}

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getRefAllele() {
        return refAllele;
    }

    public void setRefAllele(String refAllele) {
        this.refAllele = refAllele;
    }

    public String getAltAllele() {
        return altAllele;
    }

    public void setAltAllele(String altAllele) {
        this.altAllele = altAllele;
    }

    public Float getRefAlleleFrequency() {return refAlleleFrequency;}

    public void setRefAlleleFrequency(Float refAlleleFrequency) {this.refAlleleFrequency = refAlleleFrequency;}

    public Float getAltAlleleFrequency() {return altAlleleFrequency;}

    public void setAltAlleleFrequency(Float altAlleleFrequency) {this.altAlleleFrequency = altAlleleFrequency;}

    public String getGenotype1() {
        return genotype1;
    }

    public void setGenotype1(String genotype1) {
        this.genotype1 = genotype1;
    }

    public Float getGenotypeFrequency1() {
        return genotypeFrequency1;
    }

    public void setGenotypeFrequency1(Float genotypeFrequency1) {
        this.genotypeFrequency1 = genotypeFrequency1;
    }

    public String getGenotype2() {
        return genotype2;
    }

    public void setGenotype2(String genotype2) {
        this.genotype2 = genotype2;
    }

    public Float getGenotypeFrequency2() {
        return genotypeFrequency2;
    }

    public void setGenotypeFrequency2(Float genotypeFrequency2) {
        this.genotypeFrequency2 = genotypeFrequency2;
    }

    public String getGenotype3() {
        return genotype3;
    }

    public void setGenotype3(String genotype3) {
        this.genotype3 = genotype3;
    }

    public Float getGenotypeFrequency3() {
        return genotypeFrequency3;
    }

    public void setGenotypeFrequency3(Float genotypeFrequency3) {
        this.genotypeFrequency3 = genotypeFrequency3;
    }

    public String getPopulation() {return population;}

    public void setPopulation(String population) {this.population = population;}
}
