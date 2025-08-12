package big.data.bigdata.service.impl;

import big.data.bigdata.dto.SnpQueryDTO;
import big.data.bigdata.entity.SnpData;
import big.data.bigdata.mapper.SnpDataMapper;
import big.data.bigdata.service.SnpDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SnpDataServiceImpl implements SnpDataService {
    @Autowired
    private SnpDataMapper snpDataMapper;

    // 辅助方法：判断字符串是否非空
    @Override
    public boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }

    // 新增方法：根据 individual 查询数据（不合并）
    @Override
    public Map<String, Object> getSnpData(SnpQueryDTO queryDTO) {
        int chromosome = queryDTO.getChromosome();
        String population = queryDTO.getPopulation();
        int page = queryDTO.getPage();
        int size = queryDTO.getSize();
        int offset = (page - 1) * size;

        // 查询总记录数
        int total = snpDataMapper.findSnpDataCount(
                chromosome,
                queryDTO.getRsId(),
                queryDTO.getPosition(),
                population,
                queryDTO.getVariant()
        );

        // 查询当前页数据
        List<SnpData> snpDataList = snpDataMapper.findSnpData(
                chromosome,
                queryDTO.getRsId(),
                queryDTO.getPosition(),
                population,
                queryDTO.getVariant(),
                offset,
                size
        );

        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("data", snpDataList);
        result.put("page", page);
        result.put("size", size);
        result.put("total", total);
        result.put("code", 200);
        result.put("message", "ok");
        result.put("description", "");

        return result;
    }

    //新增方法：根据individual查询（合并结果）
    @Override
    public Map<String, Object> getSnpDataIndividualMerge(SnpQueryDTO queryDTO) {
        // 获取查询条件
        int chromosome = queryDTO.getChromosome(); // 从 DTO 中获取染色体编号
        String population = queryDTO.getPopulation();//从 DTO 中获取族群信息

        int page = queryDTO.getPage();  // 获取页码
        int size = queryDTO.getSize();  // 获取每页大小

        int offset = (page - 1) * size;  // 计算偏移量
        List<SnpData> snpDataList;

        if (population != null && !population.isEmpty()) {
            // 如果 population 不为空，直接查询该族群的数据
            snpDataList = snpDataMapper.findSnpData(
                    chromosome,
                    queryDTO.getRsId(),
                    queryDTO.getPosition(),
                    population,
                    queryDTO.getVariant(),
                    offset, size
            );
        } else {
            // 如果 population 为空，查询所有族群的数据，之后按族群进行分组
            snpDataList = snpDataMapper.findSnpData(
                    chromosome,
                    queryDTO.getRsId(),
                    queryDTO.getPosition(),
                    null, // 不传 population 查询所有族群
                    queryDTO.getVariant(),
                    offset, size
            );
        }

        if (snpDataList == null || snpDataList.isEmpty()) {
            return null; // 如果没有数据则返回 null
        }


        // 按 Position 和 (RefAllele, AltAllele) 和 Population 分组
        Map<String, SnpData> groupedData = new HashMap<>();
        for (SnpData snpData : snpDataList) {
            // 构建分组键：Position + RefAllele + AltAllele
            String groupKey = snpData.getPosition() + "_" + snpData.getRefAllele() + "_" + snpData.getAltAllele();
            if (population == null || population.isEmpty()) {
                // 如果是查询所有族群的情况，添加 population 到分组键中
                groupKey += "_" + snpData.getPopulation();
            }

            if (groupedData.containsKey(groupKey)) {
                // 如果分组已存在，进行合并
                SnpData existingData = groupedData.get(groupKey);
                existingData.setDataset(existingData.getDataset() + "/" + snpData.getDataset());

                int totalSampleSize = existingData.getSampleSize() + snpData.getSampleSize();
                int totalAlleleCount = existingData.getAlleleCount() + snpData.getAlleleCount(); // 合并 alleleCount

                // 初始化加权后的Ref和Alt频率累加值
                float totalRefFreq = existingData.getRefAlleleFrequency() * existingData.getSampleSize()
                        + snpData.getRefAlleleFrequency() * snpData.getSampleSize();
                float totalAltFreq = existingData.getAltAlleleFrequency() * existingData.getSampleSize()
                        + snpData.getAltAlleleFrequency() * snpData.getSampleSize();
                // 最终计算合并后的频率（保留你的代码结构）
                float mergedRefFreq = totalRefFreq / totalSampleSize;
                float mergedAltFreq = totalAltFreq / totalSampleSize;
                existingData.setRefAlleleFrequency(mergedRefFreq);
                existingData.setAltAlleleFrequency(mergedAltFreq);

                // 合并 Genotype Frequencies
                mergeGenotypeFrequencies(existingData, snpData, totalSampleSize);
                // 合并 sampleSize
                existingData.setSampleSize(totalSampleSize);
                existingData.setAlleleCount(totalAlleleCount);

            } else {
                // 如果分组不存在，直接加入
                groupedData.put(groupKey, snpData);
            }
        }

        // 返回合并后的数据列表
        List<SnpData> mergedData = new ArrayList<>(groupedData.values());

        // 计算合并后的记录总数
        int total = mergedData.size(); // 使用合并后的数据来计算总数

        // 构造返回结果，包含分页信息
        Map<String, Object> result = new HashMap<>();
        result.put("data", mergedData);
        result.put("page", page);
        result.put("size", size);
        result.put("total", total);
        result.put("code", 200);
        result.put("message", "ok");
        result.put("description", "");

        return result;
    }

    @Override
    public void mergeGenotypeFrequencies(SnpData existingData, SnpData newData, int totalSampleSize) {
        // 根据基因型重新计算频率
        for (int i = 1; i <= 3; i++) {
            String genotypeField = "genotype" + i;
            String genotypeFreqField = "genotypeFrequency" + i;

            try {
                // 使用反射获取字段值
                String genotype = (String) SnpData.class.getDeclaredMethod("get" + capitalize(genotypeField)).invoke(newData);
                Float freq = (Float) SnpData.class.getDeclaredMethod("get" + capitalize(genotypeFreqField)).invoke(newData);

                if (genotype != null && freq != null) {
                    Float existingFreq = (Float) SnpData.class.getDeclaredMethod("get" + capitalize(genotypeFreqField)).invoke(existingData);
                    float updatedFreq = (existingFreq != null ? existingFreq : 0) * existingData.getSampleSize()
                            + freq * newData.getSampleSize();
                    SnpData.class.getDeclaredMethod("set" + capitalize(genotypeFreqField), Float.class)
                            .invoke(existingData, updatedFreq / totalSampleSize);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error merging genotype frequencies: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    // 新增方法：根据 province 查询数据
    @Override
    public Map<String, Object> getSnpDataByProvince(SnpQueryDTO queryDTO) {
        String tableName = queryDTO.getProvince() + "_chr" + queryDTO.getChromosome() + "_snp_data";
        int page = queryDTO.getPage();
        int size = queryDTO.getSize();
        int offset = (page - 1) * size;

        int total;
        // 只有当 rsId、position、variant 至少一个不为空时，才去 count
        if (isNotBlank(queryDTO.getRsId()) || queryDTO.getPosition() != null || isNotBlank(queryDTO.getVariant())) {
            total = snpDataMapper.findSnpDataCountByTable(
                    tableName,
                    queryDTO.getDataset(),
                    queryDTO.getRsId(),
                    queryDTO.getPosition(),
                    queryDTO.getPopulation(),
                    queryDTO.getVariant()
            );
        } else {
            // 否则 total 设为当前查询到的数量（分页情况下也可以）
            total = 0;
        }

        // 查询当前页数据
        List<SnpData> snpDataList = snpDataMapper.findSnpDataByTable(
                tableName,
                queryDTO.getDataset(),
                queryDTO.getRsId(),
                queryDTO.getPosition(),
                queryDTO.getPopulation(),
                queryDTO.getVariant(),
                offset,
                size
        );

        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("data", snpDataList);
        result.put("page", page);
        result.put("size", size);
        result.put("total", total); // 返回整表的全部 total
        result.put("code", 200);
        result.put("message", "ok");
        result.put("description", "");

        return result;
    }

    // 新增方法：根据 province 查询数据（合并结果）
    @Override
    public Map<String, Object> getSnpDataByProvinceMerge(SnpQueryDTO queryDTO) {
        String tableName = queryDTO.getProvince() + "_chr" + queryDTO.getChromosome() + "_snp_data";
        int page = queryDTO.getPage();
        int size = queryDTO.getSize();
        int offset = (page - 1) * size;

        // 查询总记录数（整表的全部 total）
        int total;
        // 只有当 rsId、position、variant 至少一个不为空时，才去 count
        if (isNotBlank(queryDTO.getRsId()) || queryDTO.getPosition() != null || isNotBlank(queryDTO.getVariant())) {
            total = snpDataMapper.findSnpDataCountByTable(
                    tableName,
                    queryDTO.getDataset(),
                    queryDTO.getRsId(),
                    queryDTO.getPosition(),
                    queryDTO.getPopulation(),
                    queryDTO.getVariant()
            );
        } else {
            // 否则 total 设为当前查询到的数量（分页情况下也可以）
            total = 0;
        }

        // 查询当前页数据
        List<SnpData> snpDataList = snpDataMapper.findSnpDataByTable(
                tableName,
                queryDTO.getDataset(),
                queryDTO.getRsId(),
                queryDTO.getPosition(),
                queryDTO.getPopulation(),
                queryDTO.getVariant(),
                offset,
                size
        );

        if (snpDataList == null || snpDataList.isEmpty()) {
            return null; // 如果没有数据则返回 null
        }

        // 按 Position 和 (RefAllele, AltAllele) 和 Population 分组
        Map<String, SnpData> groupedData = new HashMap<>();
        for (SnpData snpData : snpDataList) {
            // 构建分组键：Position + RefAllele + AltAllele
            String groupKey = snpData.getPosition() + "_" + snpData.getRefAllele() + "_" + snpData.getAltAllele();
            if (queryDTO.getPopulation() == null || queryDTO.getPopulation().isEmpty()) {
                // 如果是查询所有族群的情况，添加 population 到分组键中
                groupKey += "_" + snpData.getPopulation();
            }

            if (groupedData.containsKey(groupKey)) {
                // 如果分组已存在，进行合并
                SnpData existingData = groupedData.get(groupKey);
                existingData.setDataset(existingData.getDataset() + "/" + snpData.getDataset());

                int totalSampleSize = existingData.getSampleSize() + snpData.getSampleSize();
                int totalAlleleCount = existingData.getAlleleCount() + snpData.getAlleleCount(); // 合并 alleleCount

                // 初始化加权后的Ref和Alt频率累加值
                float totalRefFreq = existingData.getRefAlleleFrequency() * existingData.getSampleSize()
                        + snpData.getRefAlleleFrequency() * snpData.getSampleSize();
                float totalAltFreq = existingData.getAltAlleleFrequency() * existingData.getSampleSize()
                        + snpData.getAltAlleleFrequency() * snpData.getSampleSize();
                // 最终计算合并后的频率（保留你的代码结构）
                float mergedRefFreq = totalRefFreq / totalSampleSize;
                float mergedAltFreq = totalAltFreq / totalSampleSize;
                existingData.setRefAlleleFrequency(mergedRefFreq);
                existingData.setAltAlleleFrequency(mergedAltFreq);

                // 合并 Genotype Frequencies
                mergeGenotypeFrequencies(existingData, snpData, totalSampleSize);
                // 合并 sampleSize
                existingData.setSampleSize(totalSampleSize);
                existingData.setAlleleCount(totalAlleleCount);
            } else {
                // 如果分组不存在，直接加入
                groupedData.put(groupKey, snpData);
            }
        }

        // 返回合并后的数据列表
        List<SnpData> mergedData = new ArrayList<>(groupedData.values());

        // 构造返回结果，包含分页信息
        Map<String, Object> result = new HashMap<>();
        result.put("data", mergedData);
        result.put("page", page);
        result.put("size", size);
        result.put("total", total); // 返回整表的全部 total
        result.put("code", 200);
        result.put("message", "ok");
        result.put("description", "");

        return result;
    }

    //新增方法：根据region查询数据（合并）
    // 新增方法：根据 region 查询数据（合并结果）
    @Override
    public Map<String, Object> getSnpDataByRegionMerge(SnpQueryDTO queryDTO) {
        String tableName = queryDTO.getRegion() + "_chr" + queryDTO.getChromosome() + "_snp_data";

        // 查询数据
        List<SnpData> snpDataList = snpDataMapper.findSnpDataByTable(
                tableName,
                queryDTO.getDataset(),
                queryDTO.getRsId(),
                queryDTO.getPosition(),
                queryDTO.getPopulation(),
                queryDTO.getVariant(),
                (queryDTO.getPage() - 1) * queryDTO.getSize(),
                queryDTO.getSize()
        );

        // 如果查询结果为空，则返回 null
        if (snpDataList == null || snpDataList.isEmpty()) {
            return null;
        }

        // 按 Position 和 (RefAllele, AltAllele) 和 Population 分组
        Map<String, SnpData> groupedData = new HashMap<>();
        for (SnpData snpData : snpDataList) {
            // 构建分组键：Position + RefAllele + AltAllele
            String groupKey = snpData.getPosition() + "_" + snpData.getRefAllele() + "_" + snpData.getAltAllele();
            if (queryDTO.getPopulation() == null || queryDTO.getPopulation().isEmpty()) {
                // 如果是查询所有族群的情况，添加 population 到分组键中
                groupKey += "_" + snpData.getPopulation();
            }

            if (groupedData.containsKey(groupKey)) {
                // 如果分组已存在，进行合并
                SnpData existingData = groupedData.get(groupKey);
                existingData.setDataset(existingData.getDataset() + "/" + snpData.getDataset());

                int totalSampleSize = existingData.getSampleSize() + snpData.getSampleSize();
                int totalAlleleCount = existingData.getAlleleCount() + snpData.getAlleleCount(); // 合并 alleleCount

                // 初始化加权后的Ref和Alt频率累加值
                float totalRefFreq = existingData.getRefAlleleFrequency() * existingData.getSampleSize()
                        + snpData.getRefAlleleFrequency() * snpData.getSampleSize();
                float totalAltFreq = existingData.getAltAlleleFrequency() * existingData.getSampleSize()
                        + snpData.getAltAlleleFrequency() * snpData.getSampleSize();
                // 最终计算合并后的频率（保留你的代码结构）
                float mergedRefFreq = totalRefFreq / totalSampleSize;
                float mergedAltFreq = totalAltFreq / totalSampleSize;
                existingData.setRefAlleleFrequency(mergedRefFreq);
                existingData.setAltAlleleFrequency(mergedAltFreq);

                // 合并 Genotype Frequencies
                mergeGenotypeFrequencies(existingData, snpData, totalSampleSize);
                // 合并 sampleSize
                existingData.setSampleSize(totalSampleSize);
                existingData.setAlleleCount(totalAlleleCount);
            } else {
                // 如果分组不存在，直接加入
                groupedData.put(groupKey, snpData);
            }
        }

        // 返回合并后的数据列表
        List<SnpData> mergedData = new ArrayList<>(groupedData.values());

        // 计算合并后的记录总数
        int total = mergedData.size(); // 使用合并后的数据来计算总数

        // 构造返回结果，包含分页信息
        Map<String, Object> result = new HashMap<>();
        result.put("data", mergedData);
        result.put("page", queryDTO.getPage());
        result.put("size", queryDTO.getSize());
        result.put("total", total);
        result.put("code", 200);
        result.put("message", "ok");
        result.put("description", "");

        return result;
    }

    // 新增方法：根据 region 查询数据(不合并）
    @Override
    public Map<String, Object> getSnpDataByRegion(SnpQueryDTO queryDTO) {
        String tableName = queryDTO.getRegion() + "_chr" + queryDTO.getChromosome() + "_snp_data";
        List<SnpData> snpDataList = snpDataMapper.findSnpDataByTable(
                tableName,
                queryDTO.getDataset(),
                queryDTO.getRsId(),
                queryDTO.getPosition(),
                queryDTO.getPopulation(),
                queryDTO.getVariant(),
                (queryDTO.getPage() - 1) * queryDTO.getSize(),
                queryDTO.getSize()
        );

        int total;
        // 只有当 rsId、position、variant 至少一个不为空时，才去 count
        if (isNotBlank(queryDTO.getRsId()) || queryDTO.getPosition() != null || isNotBlank(queryDTO.getVariant())) {
            total = snpDataMapper.findSnpDataCountByTable(
                    tableName,
                    queryDTO.getDataset(),
                    queryDTO.getRsId(),
                    queryDTO.getPosition(),
                    queryDTO.getPopulation(),
                    queryDTO.getVariant()
            );
        } else {
            // 否则 total 设为当前查询到的数量（分页情况下也可以）
            total = 0;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("data", snpDataList);
        result.put("page", queryDTO.getPage());
        result.put("size", queryDTO.getSize());
        result.put("total", total);
        result.put("code", 200);
        result.put("message", "ok");
        result.put("description", "");

        return result;
    }

    //新增方法：根据population查询数据（合并）
    // 新增方法：根据 population 查询数据（合并结果）
/*    @Override
    public Map<String, Object> getSnpDataByPopulation(SnpQueryDTO queryDTO) {
        int chromosome = queryDTO.getChromosome();
        String population = queryDTO.getPopulation();
        int page = queryDTO.getPage();
        int size = queryDTO.getSize();
        int offset = (page - 1) * size;

        // 查询总记录数
        int total;
        // 只有当 rsId、position、variant 至少一个不为空时，才去 count
        if (isNotBlank(queryDTO.getRsId()) || queryDTO.getPosition() != null || isNotBlank(queryDTO.getVariant())) {
            total = snpDataMapper.findSnpDataCountByPopulation(
                    chromosome,
                    queryDTO.getRsId(),
                    queryDTO.getPosition(),
                    population,
                    queryDTO.getVariant()
            );
        } else {
            // 否则 total 设为当前查询到的数量（分页情况下也可以）
            total = 0 ;
        }

        // 查询当前页数据
        List<SnpData> snpDataList = snpDataMapper.findSnpDataByPopulation(
                chromosome,
                queryDTO.getRsId(),
                queryDTO.getPosition(),
                population,
                queryDTO.getVariant(),
                offset,
                size
        );

        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("data", snpDataList);
        result.put("page", page);
        result.put("size", size);
        result.put("total", total);
        result.put("code", 200);
        result.put("message", "ok");
        result.put("description", "");

        return result;
    }

    @Override
    //新增方法：根据population查询（合并结果）
    public Map<String, Object> getSnpDataByPopulationMerge(SnpQueryDTO queryDTO) {
        // 获取查询条件
        int chromosome = queryDTO.getChromosome(); // 从 DTO 中获取染色体编号
        String population = queryDTO.getPopulation();//从 DTO 中获取族群信息

        int page = queryDTO.getPage();  // 获取页码
        int size = queryDTO.getSize();  // 获取每页大小

        int offset = (page - 1) * size;  // 计算偏移量
        List<SnpData> snpDataList;

        if (population != null && !population.isEmpty()) {
            // 如果 population 不为空，直接查询该族群的数据
            snpDataList = snpDataMapper.findSnpDataByPopulation(
                    chromosome,
                    queryDTO.getRsId(),
                    queryDTO.getPosition(),
                    population,
                    queryDTO.getVariant(),
                    offset, size
            );
        } else {
            // 如果 population 为空，查询所有族群的数据，之后按族群进行分组
            snpDataList = snpDataMapper.findSnpDataByPopulation(
                    chromosome,
                    queryDTO.getRsId(),
                    queryDTO.getPosition(),
                    null, // 不传 population 查询所有族群
                    queryDTO.getVariant(),
                    offset, size
            );
        }

        if (snpDataList == null || snpDataList.isEmpty()) {
            return null; // 如果没有数据则返回 null
        }


        // 按 Position 和 (RefAllele, AltAllele) 和 Population 分组
        Map<String, SnpData> groupedData = new HashMap<>();
        for (SnpData snpData : snpDataList) {
            // 构建分组键：Position + RefAllele + AltAllele
            String groupKey = snpData.getPosition() + "_" + snpData.getRefAllele() + "_" + snpData.getAltAllele();
            if (queryDTO.getPopulation() == null || queryDTO.getPopulation().isEmpty()) {
                // 如果是查询所有族群的情况，添加 population 到分组键中
                groupKey += "_" + snpData.getPopulation();
            }

            if (groupedData.containsKey(groupKey)) {
                // 如果分组已存在，进行合并
                SnpData existingData = groupedData.get(groupKey);
                existingData.setDataset(existingData.getDataset() + "/" + snpData.getDataset());

                int totalSampleSize = existingData.getSampleSize() + snpData.getSampleSize();

                // 初始化加权后的Ref和Alt频率累加值
                float totalRefFreq = existingData.getRefAlleleFrequency() * existingData.getSampleSize()
                        + snpData.getRefAlleleFrequency() * snpData.getSampleSize();
                float totalAltFreq = existingData.getAltAlleleFrequency() * existingData.getSampleSize()
                        + snpData.getAltAlleleFrequency() * snpData.getSampleSize();
                // 最终计算合并后的频率（保留你的代码结构）
                float mergedRefFreq = totalRefFreq / totalSampleSize;
                float mergedAltFreq = totalAltFreq / totalSampleSize;
                existingData.setRefAlleleFrequency(mergedRefFreq);
                existingData.setAltAlleleFrequency(mergedAltFreq);

                // 合并 Genotype Frequencies
                mergeGenotypeFrequencies(existingData, snpData, totalSampleSize);
                // 合并 sampleSize
                existingData.setSampleSize(totalSampleSize);
            } else {
                // 如果分组不存在，直接加入
                groupedData.put(groupKey, snpData);
            }
        }

        // 返回合并后的数据列表
        List<SnpData> mergedData = new ArrayList<>(groupedData.values());

        // 计算合并后的记录总数
        int total = mergedData.size(); // 使用合并后的数据来计算总数

        // 构造返回结果，包含分页信息
        Map<String, Object> result = new HashMap<>();
        result.put("data", mergedData);
        result.put("page", page);
        result.put("size", size);
        result.put("total", total);
        result.put("code", 200);
        result.put("message", "ok");
        result.put("description", "");

        return result;
    }*/


    //前面都是NGS的，这里开始都是芯片数据的接口
    // 新增方法：根据 individual 查询数据（不合并）
    @Override
    public Map<String, Object> getSnpDataMicroarray(SnpQueryDTO queryDTO) {
        int chromosome = queryDTO.getChromosome();
        String population = queryDTO.getPopulation();
        int page = queryDTO.getPage();
        int size = queryDTO.getSize();
        int offset = (page - 1) * size;

        // 查询总记录数
        int total = snpDataMapper.findSnpDataCount(
                chromosome,
                queryDTO.getRsId(),
                queryDTO.getPosition(),
                population,
                queryDTO.getVariant()
        );

        // 查询当前页数据
        List<SnpData> snpDataList = snpDataMapper.findSnpData(
                chromosome,
                queryDTO.getRsId(),
                queryDTO.getPosition(),
                population,
                queryDTO.getVariant(),
                offset,
                size
        );

        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("data", snpDataList);
        result.put("page", page);
        result.put("size", size);
        result.put("total", total);
        result.put("code", 200);
        result.put("message", "ok");
        result.put("description", "");

        return result;
    }

    //新增方法：根据individual查询（合并结果）
    @Override
    public Map<String, Object> getSnpDataIndividualMergeMicroarray(SnpQueryDTO queryDTO) {
        // 获取查询条件
        int chromosome = queryDTO.getChromosome(); // 从 DTO 中获取染色体编号
        String population = queryDTO.getPopulation();//从 DTO 中获取族群信息

        int page = queryDTO.getPage();  // 获取页码
        int size = queryDTO.getSize();  // 获取每页大小

        int offset = (page - 1) * size;  // 计算偏移量
        List<SnpData> snpDataList;

        if (population != null && !population.isEmpty()) {
            // 如果 population 不为空，直接查询该族群的数据
            snpDataList = snpDataMapper.findSnpData(
                    chromosome,
                    queryDTO.getRsId(),
                    queryDTO.getPosition(),
                    population,
                    queryDTO.getVariant(),
                    offset, size
            );
        } else {
            // 如果 population 为空，查询所有族群的数据，之后按族群进行分组
            snpDataList = snpDataMapper.findSnpData(
                    chromosome,
                    queryDTO.getRsId(),
                    queryDTO.getPosition(),
                    null, // 不传 population 查询所有族群
                    queryDTO.getVariant(),
                    offset, size
            );
        }

        if (snpDataList == null || snpDataList.isEmpty()) {
            return null; // 如果没有数据则返回 null
        }


        // 按 Position 和 (RefAllele, AltAllele) 和 Population 分组
        Map<String, SnpData> groupedData = new HashMap<>();
        for (SnpData snpData : snpDataList) {
            // 构建分组键：Position + RefAllele + AltAllele
            String groupKey = snpData.getPosition() + "_" + snpData.getRefAllele() + "_" + snpData.getAltAllele();
            if (population == null || population.isEmpty()) {
                // 如果是查询所有族群的情况，添加 population 到分组键中
                groupKey += "_" + snpData.getPopulation();
            }

            if (groupedData.containsKey(groupKey)) {
                // 如果分组已存在，进行合并
                SnpData existingData = groupedData.get(groupKey);
                existingData.setDataset(existingData.getDataset() + "/" + snpData.getDataset());

                int totalSampleSize = existingData.getSampleSize() + snpData.getSampleSize();
                int totalAlleleCount = existingData.getAlleleCount() + snpData.getAlleleCount(); // 合并 alleleCount

                // 初始化加权后的Ref和Alt频率累加值
                float totalRefFreq = existingData.getRefAlleleFrequency() * existingData.getSampleSize()
                        + snpData.getRefAlleleFrequency() * snpData.getSampleSize();
                float totalAltFreq = existingData.getAltAlleleFrequency() * existingData.getSampleSize()
                        + snpData.getAltAlleleFrequency() * snpData.getSampleSize();
                // 最终计算合并后的频率（保留你的代码结构）
                float mergedRefFreq = totalRefFreq / totalSampleSize;
                float mergedAltFreq = totalAltFreq / totalSampleSize;
                existingData.setRefAlleleFrequency(mergedRefFreq);
                existingData.setAltAlleleFrequency(mergedAltFreq);

                // 合并 Genotype Frequencies
                mergeGenotypeFrequencies(existingData, snpData, totalSampleSize);
                // 合并 sampleSize
                existingData.setSampleSize(totalSampleSize);
                existingData.setAlleleCount(totalAlleleCount);

            } else {
                // 如果分组不存在，直接加入
                groupedData.put(groupKey, snpData);
            }
        }

        // 返回合并后的数据列表
        List<SnpData> mergedData = new ArrayList<>(groupedData.values());

        // 计算合并后的记录总数
        int total = mergedData.size(); // 使用合并后的数据来计算总数

        // 构造返回结果，包含分页信息
        Map<String, Object> result = new HashMap<>();
        result.put("data", mergedData);
        result.put("page", page);
        result.put("size", size);
        result.put("total", total);
        result.put("code", 200);
        result.put("message", "ok");
        result.put("description", "");

        return result;
    }
    // 新增方法：根据 province 查询数据
    @Override
    public Map<String, Object> getSnpDataByProvinceMicroarray(SnpQueryDTO queryDTO) {
        String tableName = queryDTO.getProvince() + "_chr" + queryDTO.getChromosome() + "_snp_data";
        int page = queryDTO.getPage();
        int size = queryDTO.getSize();
        int offset = (page - 1) * size;

        int total;
        // 只有当 rsId、position、variant 至少一个不为空时，才去 count
        if (isNotBlank(queryDTO.getRsId()) || queryDTO.getPosition() != null || isNotBlank(queryDTO.getVariant())) {
            total = snpDataMapper.findSnpDataCountByTable(
                    tableName,
                    queryDTO.getDataset(),
                    queryDTO.getRsId(),
                    queryDTO.getPosition(),
                    queryDTO.getPopulation(),
                    queryDTO.getVariant()
            );
        } else {
            // 否则 total 设为当前查询到的数量（分页情况下也可以）
            total = 0;
        }

        // 查询当前页数据
        List<SnpData> snpDataList = snpDataMapper.findSnpDataByTable(
                tableName,
                queryDTO.getDataset(),
                queryDTO.getRsId(),
                queryDTO.getPosition(),
                queryDTO.getPopulation(),
                queryDTO.getVariant(),
                offset,
                size
        );

        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("data", snpDataList);
        result.put("page", page);
        result.put("size", size);
        result.put("total", total); // 返回整表的全部 total
        result.put("code", 200);
        result.put("message", "ok");
        result.put("description", "");

        return result;
    }

    // 新增方法：根据 province 查询数据（合并结果）
    @Override
    public Map<String, Object> getSnpDataByProvinceMergeMicroarray(SnpQueryDTO queryDTO) {
        String tableName = queryDTO.getProvince() + "_chr" + queryDTO.getChromosome() + "_snp_data";
        int page = queryDTO.getPage();
        int size = queryDTO.getSize();
        int offset = (page - 1) * size;

        // 查询总记录数（整表的全部 total）
        int total;
        // 只有当 rsId、position、variant 至少一个不为空时，才去 count
        if (isNotBlank(queryDTO.getRsId()) || queryDTO.getPosition() != null || isNotBlank(queryDTO.getVariant())) {
            total = snpDataMapper.findSnpDataCountByTable(
                    tableName,
                    queryDTO.getDataset(),
                    queryDTO.getRsId(),
                    queryDTO.getPosition(),
                    queryDTO.getPopulation(),
                    queryDTO.getVariant()
            );
        } else {
            // 否则 total 设为当前查询到的数量（分页情况下也可以）
            total = 0;
        }

        // 查询当前页数据
        List<SnpData> snpDataList = snpDataMapper.findSnpDataByTable(
                tableName,
                queryDTO.getDataset(),
                queryDTO.getRsId(),
                queryDTO.getPosition(),
                queryDTO.getPopulation(),
                queryDTO.getVariant(),
                offset,
                size
        );

        if (snpDataList == null || snpDataList.isEmpty()) {
            return null; // 如果没有数据则返回 null
        }

        // 按 Position 和 (RefAllele, AltAllele) 和 Population 分组
        Map<String, SnpData> groupedData = new HashMap<>();
        for (SnpData snpData : snpDataList) {
            // 构建分组键：Position + RefAllele + AltAllele
            String groupKey = snpData.getPosition() + "_" + snpData.getRefAllele() + "_" + snpData.getAltAllele();
            if (queryDTO.getPopulation() == null || queryDTO.getPopulation().isEmpty()) {
                // 如果是查询所有族群的情况，添加 population 到分组键中
                groupKey += "_" + snpData.getPopulation();
            }

            if (groupedData.containsKey(groupKey)) {
                // 如果分组已存在，进行合并
                SnpData existingData = groupedData.get(groupKey);
                existingData.setDataset(existingData.getDataset() + "/" + snpData.getDataset());

                int totalSampleSize = existingData.getSampleSize() + snpData.getSampleSize();
                int totalAlleleCount = existingData.getAlleleCount() + snpData.getAlleleCount(); // 合并 alleleCount

                // 初始化加权后的Ref和Alt频率累加值
                float totalRefFreq = existingData.getRefAlleleFrequency() * existingData.getSampleSize()
                        + snpData.getRefAlleleFrequency() * snpData.getSampleSize();
                float totalAltFreq = existingData.getAltAlleleFrequency() * existingData.getSampleSize()
                        + snpData.getAltAlleleFrequency() * snpData.getSampleSize();
                // 最终计算合并后的频率（保留你的代码结构）
                float mergedRefFreq = totalRefFreq / totalSampleSize;
                float mergedAltFreq = totalAltFreq / totalSampleSize;
                existingData.setRefAlleleFrequency(mergedRefFreq);
                existingData.setAltAlleleFrequency(mergedAltFreq);

                // 合并 Genotype Frequencies
                mergeGenotypeFrequencies(existingData, snpData, totalSampleSize);
                // 合并 sampleSize
                existingData.setSampleSize(totalSampleSize);
                existingData.setAlleleCount(totalAlleleCount);
            } else {
                // 如果分组不存在，直接加入
                groupedData.put(groupKey, snpData);
            }
        }

        // 返回合并后的数据列表
        List<SnpData> mergedData = new ArrayList<>(groupedData.values());

        // 构造返回结果，包含分页信息
        Map<String, Object> result = new HashMap<>();
        result.put("data", mergedData);
        result.put("page", page);
        result.put("size", size);
        result.put("total", total); // 返回整表的全部 total
        result.put("code", 200);
        result.put("message", "ok");
        result.put("description", "");

        return result;
    }

    //新增方法：根据region查询数据（合并）
    // 新增方法：根据 region 查询数据（合并结果）
    @Override
    public Map<String, Object> getSnpDataByRegionMergeMicroarray(SnpQueryDTO queryDTO) {
        String tableName = queryDTO.getRegion() + "_chr" + queryDTO.getChromosome() + "_snp_data";

        // 查询数据
        List<SnpData> snpDataList = snpDataMapper.findSnpDataByTable(
                tableName,
                queryDTO.getDataset(),
                queryDTO.getRsId(),
                queryDTO.getPosition(),
                queryDTO.getPopulation(),
                queryDTO.getVariant(),
                (queryDTO.getPage() - 1) * queryDTO.getSize(),
                queryDTO.getSize()
        );

        // 如果查询结果为空，则返回 null
        if (snpDataList == null || snpDataList.isEmpty()) {
            return null;
        }

        // 按 Position 和 (RefAllele, AltAllele) 和 Population 分组
        Map<String, SnpData> groupedData = new HashMap<>();
        for (SnpData snpData : snpDataList) {
            // 构建分组键：Position + RefAllele + AltAllele
            String groupKey = snpData.getPosition() + "_" + snpData.getRefAllele() + "_" + snpData.getAltAllele();
            if (queryDTO.getPopulation() == null || queryDTO.getPopulation().isEmpty()) {
                // 如果是查询所有族群的情况，添加 population 到分组键中
                groupKey += "_" + snpData.getPopulation();
            }

            if (groupedData.containsKey(groupKey)) {
                // 如果分组已存在，进行合并
                SnpData existingData = groupedData.get(groupKey);
                existingData.setDataset(existingData.getDataset() + "/" + snpData.getDataset());

                int totalSampleSize = existingData.getSampleSize() + snpData.getSampleSize();
                int totalAlleleCount = existingData.getAlleleCount() + snpData.getAlleleCount(); // 合并 alleleCount

                // 初始化加权后的Ref和Alt频率累加值
                float totalRefFreq = existingData.getRefAlleleFrequency() * existingData.getSampleSize()
                        + snpData.getRefAlleleFrequency() * snpData.getSampleSize();
                float totalAltFreq = existingData.getAltAlleleFrequency() * existingData.getSampleSize()
                        + snpData.getAltAlleleFrequency() * snpData.getSampleSize();
                // 最终计算合并后的频率（保留你的代码结构）
                float mergedRefFreq = totalRefFreq / totalSampleSize;
                float mergedAltFreq = totalAltFreq / totalSampleSize;
                existingData.setRefAlleleFrequency(mergedRefFreq);
                existingData.setAltAlleleFrequency(mergedAltFreq);

                // 合并 Genotype Frequencies
                mergeGenotypeFrequencies(existingData, snpData, totalSampleSize);
                // 合并 sampleSize
                existingData.setSampleSize(totalSampleSize);
                existingData.setAlleleCount(totalAlleleCount);
            } else {
                // 如果分组不存在，直接加入
                groupedData.put(groupKey, snpData);
            }
        }

        // 返回合并后的数据列表
        List<SnpData> mergedData = new ArrayList<>(groupedData.values());

        // 计算合并后的记录总数
        int total = mergedData.size(); // 使用合并后的数据来计算总数

        // 构造返回结果，包含分页信息
        Map<String, Object> result = new HashMap<>();
        result.put("data", mergedData);
        result.put("page", queryDTO.getPage());
        result.put("size", queryDTO.getSize());
        result.put("total", total);
        result.put("code", 200);
        result.put("message", "ok");
        result.put("description", "");

        return result;
    }

    // 新增方法：根据 region 查询数据(不合并）
    @Override
    public Map<String, Object> getSnpDataByRegionMicroarray(SnpQueryDTO queryDTO) {
        String tableName = queryDTO.getRegion() + "_chr" + queryDTO.getChromosome() + "_snp_data";
        List<SnpData> snpDataList = snpDataMapper.findSnpDataByTable(
                tableName,
                queryDTO.getDataset(),
                queryDTO.getRsId(),
                queryDTO.getPosition(),
                queryDTO.getPopulation(),
                queryDTO.getVariant(),
                (queryDTO.getPage() - 1) * queryDTO.getSize(),
                queryDTO.getSize()
        );

        int total;
        // 只有当 rsId、position、variant 至少一个不为空时，才去 count
        if (isNotBlank(queryDTO.getRsId()) || queryDTO.getPosition() != null || isNotBlank(queryDTO.getVariant())) {
            total = snpDataMapper.findSnpDataCountByTable(
                    tableName,
                    queryDTO.getDataset(),
                    queryDTO.getRsId(),
                    queryDTO.getPosition(),
                    queryDTO.getPopulation(),
                    queryDTO.getVariant()
            );
        } else {
            // 否则 total 设为当前查询到的数量（分页情况下也可以）
            total = 0;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("data", snpDataList);
        result.put("page", queryDTO.getPage());
        result.put("size", queryDTO.getSize());
        result.put("total", total);
        result.put("code", 200);
        result.put("message", "ok");
        result.put("description", "");

        return result;
    }
}
