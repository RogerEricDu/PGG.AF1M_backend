package big.data.bigdata.dto;

public class SNPAnnotationResult {
    private String snp_id;
    private String variant_type;
    private String consequence;
    private String impact;
    private String geneSymbol;
    private String sift_prediction;
    private String gene_function;
    private String polyphen_prediction;
    private String clinical_significance;

    public String getSnp_id() {
        return snp_id;
    }

    public void setSnp_id(String snp_id) {
        this.snp_id = snp_id;
    }

    public String getVariant_type() {
        return variant_type;
    }

    public void setVariant_type(String variant_type) {
        this.variant_type = variant_type;
    }

    public String getConsequence() {
        return consequence;
    }

    public void setConsequence(String consequence) {
        this.consequence = consequence;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public String getGene_symbol() {
        return geneSymbol;
    }

    public void setGene_symbol(String gene_symbol) {
        this.geneSymbol = gene_symbol;
    }

    public String getSift_prediction() {
        return sift_prediction;
    }

    public void setSift_prediction(String sift_prediction) {
        this.sift_prediction = sift_prediction;
    }

    public String getGene_function() {
        return gene_function;
    }

    public void setGene_function(String gene_function) {
        this.gene_function = gene_function;
    }

    public String getPolyphen_prediction() {
        return polyphen_prediction;
    }

    public void setPolyphen_prediction(String polyphen_prediction) {
        this.polyphen_prediction = polyphen_prediction;
    }

    public String getClinical_significance() {
        return clinical_significance;
    }

    public void setClinical_significance(String clinical_significance) {
        this.clinical_significance = clinical_significance;
    }
}
