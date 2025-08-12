package big.data.bigdata.dto;

public class SnpQueryDTO {
    private Integer chromosome;
    private String rsId;
    private Integer position;
    private String variant;
    private String province;    // 仅在 "/province" 接口使用
    private String region;      // 仅在 "/region" 接口使用
    private String dataset;     // 数据集
    private String population;

    private Integer page; //当前页
    private Integer size; //每页大小
    private String dataType;
    private String dataLayer;
    private String referencePanel;

    public Integer getChromosome() {
        return chromosome;
    }

    public void setChromosome(Integer chromosome) {
        this.chromosome = chromosome;
    }

    public String getRsId() {
        return rsId;
    }

    public void setRsId(String rsId) {
        this.rsId = rsId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getVariant() {return variant;}

    public void setVariant(String variant) {this.variant = variant;}

    public String getProvince() {return province;}

    public void setProvince(String province) {this.province = province;}

    public String getRegion() {return region;}

    public void setRegion(String region) {this.region = region;}

    public String getDataset() {return dataset;}

    public void setDataset(String dataset) {this.dataset = dataset;}

    public String getPopulation() {return population;}

    public void setPopulation(String population) {this.population = population;}

    public Integer getPage() {return page;}

    public void setPage(Integer page) {this.page = page;}

    public Integer getSize() {return size;}

    public void setSize(Integer size) {this.size = size;}

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataLayer() {
        return dataLayer;
    }

    public void setDataLayer(String dataLayer) {
        this.dataLayer = dataLayer;
    }

    public String getReferencePanel() {
        return referencePanel;
    }

    public void setReferencePanel(String referencePanel) {
        this.referencePanel = referencePanel;
    }
}
