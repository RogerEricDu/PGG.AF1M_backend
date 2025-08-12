package big.data.bigdata.entity;

public class VariantEffect {
    private Integer position;
    private String refAllele;
    private String altAllele;
    private Long snpId;
    private String symbol;
    private String gene;
    private String exon;
    private Integer cDNAPosition;
    private String biotype;
    private String consequence;
    private String feature;
    private String featureType;
    private Integer featureStrand;


    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
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

    public Long getSnpId() {
        return snpId;
    }

    public void setSnpId(Long snpId) {
        this.snpId = snpId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

    public String getExon() {
        return exon;
    }

    public void setExon(String exon) {
        this.exon = exon;
    }

    public Integer getcDNAPosition() {
        return cDNAPosition;
    }

    public void setcDNAPosition(Integer cDNAPosition) {
        this.cDNAPosition = cDNAPosition;
    }

    public String getBiotype() {
        return biotype;
    }

    public void setBiotype(String biotype) {
        this.biotype = biotype;
    }

    public String getConsequence() {
        return consequence;
    }

    public void setConsequence(String consequence) {
        this.consequence = consequence;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getFeatureType() {
        return featureType;
    }

    public void setFeatureType(String featureType) {
        this.featureType = featureType;
    }

    public Integer getFeatureStrand() {
        return featureStrand;
    }

    public void setFeatureStrand(Integer featureStrand) {
        this.featureStrand = featureStrand;
    }
}
