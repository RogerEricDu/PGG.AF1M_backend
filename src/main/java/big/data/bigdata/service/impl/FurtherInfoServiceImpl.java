package big.data.bigdata.service.impl;

import big.data.bigdata.dto.GlobalSnpQueryDTO;
import big.data.bigdata.dto.SnpQueryDTO;
import big.data.bigdata.entity.GeneticSubgroups;
import big.data.bigdata.entity.GlobalSnpData;
import big.data.bigdata.entity.ProvincialPopulation;
import big.data.bigdata.entity.VariantEffect;
import big.data.bigdata.mapper.GeneticSubgroupsMapper;
import big.data.bigdata.mapper.GlobalMapMapper;
import big.data.bigdata.mapper.ProvincialPopulationMapper;
import big.data.bigdata.mapper.VariantEffectMapper;
import big.data.bigdata.service.FurtherInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;



@Service
public class FurtherInfoServiceImpl implements FurtherInfoService {
    @Autowired
    private VariantEffectMapper variantEffectMapper;
    @Autowired
    private ProvincialPopulationMapper provincialPopulationMapper;
    @Autowired
    private GeneticSubgroupsMapper geneticSubgroupsMapper;
    @Autowired
    private GlobalMapMapper globalMapMapper;

    private static final List<String> PROVINCES = Arrays.asList(
            "Beijing", "Tianjin", "Shanghai", "Chongqing", "Anhui", "Fujian", "Gansu",
            "Guangdong", "Guangxi", "Guizhou", "Hainan", "Hebei", "Heilongjiang",
            "Henan", "Hubei", "Hunan", "Jiangsu", "Jiangxi", "Jilin", "Liaoning",
            "Ningxia", "Qinghai", "Shaanxi", "Shandong", "Shanxi", "Sichuan",
            "Tibet", "Xinjiang", "Yunnan", "Zhejiang", "HongKong", "Macau",
            "Taiwan", "InnerMongolia", "Singapore"
    );

    private static final List<String> REGIONS = Arrays.asList(
            "central", "northeast", "northwest", "southcoast", "southwest", "southeast"
    );

    @Override
    public Map<String, Object> getVariantEffect(SnpQueryDTO queryDTO) {
        // 获取查询条件
        int chromosome = queryDTO.getChromosome();
        int position = queryDTO.getPosition();
        int page = queryDTO.getPage();
        int size = queryDTO.getSize();

        int offset = (page - 1) * size;
        int limit = size;

        // 查询variant effect数据
        List<VariantEffect> variantEffects = variantEffectMapper.findVariantEffectsByChromosomeAndPosition(
                chromosome, position, offset, limit);
        long total = variantEffectMapper.countVariantEffectsByChromosomeAndPosition(chromosome, position);

        // 构造返回结果，包含分页信息
        Map<String, Object> result = new HashMap<>();
        result.put("data", variantEffects);
        result.put("page", page);
        result.put("size", size);
        result.put("total", total);
        result.put("code", 200);
        result.put("message", "ok");
        result.put("description", "");

        return result;
    }

    @Override
    public Map<String, Object> getProvincialPopulation(SnpQueryDTO queryDTO) {
        Integer position = queryDTO.getPosition();
        String population = queryDTO.getPopulation();
        int page = queryDTO.getPage();
        int size = queryDTO.getSize();

        Map<String, List<ProvincialPopulation>> provinceDataMap = new HashMap<>();

        for (String province : PROVINCES) {
            String tableName = province + "_chr" + queryDTO.getChromosome() + "_snp_data";

            List<ProvincialPopulation> provincialPopulations = provincialPopulationMapper
                    .findProvincialPopulationByChromosomeAndPosition(tableName, position, 0, Integer.MAX_VALUE, population);

            if (provincialPopulations != null && !provincialPopulations.isEmpty()) {
                Map<String, ProvincialPopulation> groupedData = new HashMap<>();

                for (ProvincialPopulation populationRecord : provincialPopulations) {
                    String key = populationRecord.getPosition() + "_" + populationRecord.getRefAllele() + "_" + populationRecord.getAltAllele();

                    if (groupedData.containsKey(key)) {
                        ProvincialPopulation existing = groupedData.get(key);

                        int count1 = existing.getAlleleCount();
                        int count2 = populationRecord.getAlleleCount();
                        int totalAlleleCount = count1 + count2;

                        // 加权平均计算频率
                        float freq1 = (existing.getGenotypeFrequency1() * count1 + populationRecord.getGenotypeFrequency1() * count2) / totalAlleleCount;
                        float freq2 = (existing.getGenotypeFrequency2() * count1 + populationRecord.getGenotypeFrequency2() * count2) / totalAlleleCount;
                        float freq3 = (existing.getGenotypeFrequency3() * count1 + populationRecord.getGenotypeFrequency3() * count2) / totalAlleleCount;
                        float refFreq = (existing.getRefAlleleFrequency() * count1 + populationRecord.getRefAlleleFrequency() * count2) / totalAlleleCount;
                        float altFreq = (existing.getAltAlleleFrequency() * count1 + populationRecord.getAltAlleleFrequency() * count2) / totalAlleleCount;


                        // 设置合并后值
                        existing.setAlleleCount(totalAlleleCount);
                        existing.setGenotypeFrequency1(freq1);
                        existing.setGenotypeFrequency2(freq2);
                        existing.setGenotypeFrequency3(freq3);
                        existing.setRefAlleleFrequency(refFreq);
                        existing.setAltAlleleFrequency(altFreq);

                        // 保留初始 genotype（不合并）
                        // 如果你希望保留出现最多的 genotype，可以使用 Map<String, Integer> 做计数统计
                    } else {
                        // 克隆对象（避免原对象复用引发错误）
                        ProvincialPopulation newPop = new ProvincialPopulation();
                        BeanUtils.copyProperties(populationRecord, newPop);
                        groupedData.put(key, newPop);
                    }
                }

                // 频率归一化（可选）
                for (ProvincialPopulation pop : groupedData.values()) {
                    float sum = pop.getGenotypeFrequency1() + pop.getGenotypeFrequency2() + pop.getGenotypeFrequency3();
                    if (sum > 0) {
                        pop.setGenotypeFrequency1(pop.getGenotypeFrequency1() / sum);
                        pop.setGenotypeFrequency2(pop.getGenotypeFrequency2() / sum);
                        pop.setGenotypeFrequency3(pop.getGenotypeFrequency3() / sum);
                    }
                }

                provinceDataMap.put(province, new ArrayList<>(groupedData.values()));
            } else {
                provinceDataMap.put(province, new ArrayList<>());
            }
        }

        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();

        for (String province : PROVINCES) {
            Map<String, Object> provinceData = new HashMap<>();
            provinceData.put("province", province);
            provinceData.put("data", provinceDataMap.get(province));
            data.add(provinceData);
        }

        result.put("data", data);
        result.put("code", 200);
        result.put("message", "ok");
        result.put("description", "");

        return result;
    }

    @Override
    public Map<String, Object> getGeneticSubgroups(SnpQueryDTO queryDTO) {
        Integer position = queryDTO.getPosition();
        String population = queryDTO.getPopulation();
        int page = queryDTO.getPage();
        int size = queryDTO.getSize();

        Map<String, List<GeneticSubgroups>> regionDataMap = new HashMap<>();

        for (String region : REGIONS) {
            String tableName = region + "_chr" + queryDTO.getChromosome() + "_snp_data";
            List<GeneticSubgroups> geneticSubgroups = geneticSubgroupsMapper
                    .findGeneticSubgroupsByChromosomeAndPosition(tableName, position, 0, Integer.MAX_VALUE,population);

            if (geneticSubgroups != null && !geneticSubgroups.isEmpty()) {
                Map<String, GeneticSubgroups> groupedData = new HashMap<>();

                for (GeneticSubgroups record : geneticSubgroups) {
                    String key = record.getPosition() + "_" + record.getRefAllele() + "_" + record.getAltAllele();

                    if (groupedData.containsKey(key)) {
                        GeneticSubgroups existing = groupedData.get(key);

                        int count1 = existing.getAlleleCount();
                        int count2 = record.getAlleleCount();
                        int totalAlleleCount = count1 + count2;

                        if (totalAlleleCount > 0) {
                            float freq1 = (existing.getGenotypeFrequency1() * count1 + record.getGenotypeFrequency1() * count2) / totalAlleleCount;
                            float freq2 = (existing.getGenotypeFrequency2() * count1 + record.getGenotypeFrequency2() * count2) / totalAlleleCount;
                            float freq3 = (existing.getGenotypeFrequency3() * count1 + record.getGenotypeFrequency3() * count2) / totalAlleleCount;
                            float refFreq = (existing.getRefAlleleFrequency() * count1 + record.getRefAlleleFrequency() * count2) / totalAlleleCount;
                            float altFreq = (existing.getAltAlleleFrequency() * count1 + record.getAltAlleleFrequency() * count2) / totalAlleleCount;
                            existing.setAlleleCount(totalAlleleCount);
                            existing.setGenotypeFrequency1(freq1);
                            existing.setGenotypeFrequency2(freq2);
                            existing.setGenotypeFrequency3(freq3);
                            existing.setRefAlleleFrequency(refFreq);
                            existing.setAltAlleleFrequency(altFreq);
                        }

                    } else {
                        // 克隆对象，避免原始对象被后续修改
                        GeneticSubgroups newSubgroup = new GeneticSubgroups();
                        BeanUtils.copyProperties(record, newSubgroup);
                        groupedData.put(key, newSubgroup);
                    }
                }

                // 归一化频率
                for (GeneticSubgroups item : groupedData.values()) {
                    float sum = item.getGenotypeFrequency1() + item.getGenotypeFrequency2() + item.getGenotypeFrequency3();
                    if (sum > 0) {
                        item.setGenotypeFrequency1(item.getGenotypeFrequency1() / sum);
                        item.setGenotypeFrequency2(item.getGenotypeFrequency2() / sum);
                        item.setGenotypeFrequency3(item.getGenotypeFrequency3() / sum);
                    }
                }

                regionDataMap.put(region, new ArrayList<>(groupedData.values()));
            } else {
                regionDataMap.put(region, new ArrayList<>());
            }
        }

        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();

        for (String region : REGIONS) {
            Map<String, Object> regionData = new HashMap<>();
            regionData.put("region", region);
            regionData.put("data", regionDataMap.get(region));
            data.add(regionData);
        }

        result.put("data", data);
        result.put("code", 200);
        result.put("message", "ok");
        result.put("description", "");

        return result;
    }

    @Override
    public Map<String, Object> getGlobalMap(GlobalSnpQueryDTO globalSnpQueryDTO) {
        int chromosome = globalSnpQueryDTO.getChromosome();
        Integer position = globalSnpQueryDTO.getPosition();

        // 查询所有满足条件的数据（可能有多个国家、多个 dataset）
        List<GlobalSnpData> rawDataList = globalMapMapper.findGlobalSnpData(chromosome, position, null);
        if (rawDataList == null || rawDataList.isEmpty()) {
            return Map.of("code", 404, "message", "No data found", "data", List.of());
        }
        // 按 country 分组并合并
        Map<String, GlobalSnpData> countryGrouped = new HashMap<>();
        for (GlobalSnpData snp : rawDataList) {
            String country = snp.getCountry();
            if (!countryGrouped.containsKey(country)) {
                countryGrouped.put(country, snp);
                continue;
            }

            GlobalSnpData existing = countryGrouped.get(country);

            int totalSampleSize = existing.getSampleSize() + snp.getSampleSize();
            int totalAlleleCount = existing.getAlleleCount() + snp.getAlleleCount();

            // 加权等位基因频率
            float mergedRefFreq = (
                    existing.getRefAlleleFrequency() * existing.getSampleSize()
                            + snp.getRefAlleleFrequency() * snp.getSampleSize()
            ) / totalSampleSize;

            float mergedAltFreq = (
                    existing.getAltAlleleFrequency() * existing.getSampleSize()
                            + snp.getAltAlleleFrequency() * snp.getSampleSize()
            ) / totalSampleSize;

            // 加权 Genotype Frequencies
            float g1 = (
                    existing.getGenotypeFrequency1() * existing.getSampleSize()
                            + snp.getGenotypeFrequency1() * snp.getSampleSize()
            ) / totalSampleSize;

            float g2 = (
                    existing.getGenotypeFrequency2() * existing.getSampleSize()
                            + snp.getGenotypeFrequency2() * snp.getSampleSize()
            ) / totalSampleSize;

            float g3 = (
                    existing.getGenotypeFrequency3() * existing.getSampleSize()
                            + snp.getGenotypeFrequency3() * snp.getSampleSize()
            ) / totalSampleSize;

            // 合并数据
            existing.setSampleSize(totalSampleSize);
            existing.setAlleleCount(totalAlleleCount);
            existing.setRefAlleleFrequency(mergedRefFreq);
            existing.setAltAlleleFrequency(mergedAltFreq);
            existing.setGenotypeFrequency1(g1);
            existing.setGenotypeFrequency2(g2);
            existing.setGenotypeFrequency3(g3);
            existing.setDataset(existing.getDataset() + "/" + snp.getDataset());
        }

        // 构造返回值
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("description", "");
        result.put("data", new ArrayList<>(countryGrouped.values()));

        return result;
    }
}
