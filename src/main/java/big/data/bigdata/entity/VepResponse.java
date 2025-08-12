package big.data.bigdata.entity;

public class VepResponse {
    private String variantId;
    private String gene;
    private String consequence;
    private String hgvsC;
    private String hgvsP;

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

    public String getConsequence() {
        return consequence;
    }

    public void setConsequence(String consequence) {
        this.consequence = consequence;
    }

    public String getHgvsC() {
        return hgvsC;
    }

    public void setHgvsC(String hgvsC) {
        this.hgvsC = hgvsC;
    }

    public String getHgvsP() {
        return hgvsP;
    }

    public void setHgvsP(String hgvsP) {
        this.hgvsP = hgvsP;
    }
}
