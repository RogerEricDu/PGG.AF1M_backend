package big.data.bigdata.dto;

public class SNPAnnotationRequest {
    private String chromosome;  // 例如 "chr19"
    private String position;    // 例如 "45411941"
    private String referenceAllele; // 可选字段
    private String alternateAllele; // 可选字段

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getReferenceAllele() {
        return referenceAllele;
    }

    public void setReferenceAllele(String referenceAllele) {
        this.referenceAllele = referenceAllele;
    }

    public String getAlternateAllele() {
        return alternateAllele;
    }

    public void setAlternateAllele(String alternateAllele) {
        this.alternateAllele = alternateAllele;
    }
}
