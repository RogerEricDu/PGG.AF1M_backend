package big.data.bigdata.service;

import big.data.bigdata.dto.SnpQueryDTO;
import big.data.bigdata.entity.SnpData;

import java.util.Map;

public interface SnpDataService {
    /**
     * 判断字符串是否非空
     */
    boolean isNotBlank(String str);

    /**
     * 根据individual查询SNP数据(不合并)
     */
    Map<String, Object> getSnpData(SnpQueryDTO queryDTO);

    /**
     * 根据individual查询SNP数据(合并结果)
     */
    Map<String, Object> getSnpDataIndividualMerge(SnpQueryDTO queryDTO);

    /**
     * 根据省份查询SNP数据
     */
    Map<String, Object> getSnpDataByProvince(SnpQueryDTO queryDTO);

    /**
     * 根据省份查询SNP数据(合并结果)
     */
    Map<String, Object> getSnpDataByProvinceMerge(SnpQueryDTO queryDTO);

    /**
     * 根据地区查询SNP数据(合并结果)
     */
    Map<String, Object> getSnpDataByRegionMerge(SnpQueryDTO queryDTO);

    /**
     * 根据地区查询SNP数据(不合并)
     */
    Map<String, Object> getSnpDataByRegion(SnpQueryDTO queryDTO);

/*    *//**
     * 根据族群查询SNP数据
     *//*
    Map<String, Object> getSnpDataByPopulation(SnpQueryDTO queryDTO);

    *//**
     * 根据族群查询SNP数据(合并结果)
     *//*
    Map<String, Object> getSnpDataByPopulationMerge(SnpQueryDTO queryDTO);*/

    /**
     * 合并基因型频率(内部方法)
     */
    void mergeGenotypeFrequencies(SnpData existingData, SnpData newData, int totalSampleSize);


    //前面都是NGS数据的，后面都是芯片数据的接口，这里merge的逻辑共用同一个就行，因为字段都是一样的

    /**
     * 根据individual查询SNP数据(不合并)
     */
    Map<String, Object> getSnpDataMicroarray(SnpQueryDTO queryDTO);

    /**
     * 根据individual查询SNP数据(合并结果)
     */
    Map<String, Object> getSnpDataIndividualMergeMicroarray(SnpQueryDTO queryDTO);

    /**
     * 根据省份查询SNP数据
     */
    Map<String, Object> getSnpDataByProvinceMicroarray(SnpQueryDTO queryDTO);

    /**
     * 根据省份查询SNP数据(合并结果)
     */
    Map<String, Object> getSnpDataByProvinceMergeMicroarray(SnpQueryDTO queryDTO);

    /**
     * 根据地区查询SNP数据(合并结果)
     */
    Map<String, Object> getSnpDataByRegionMergeMicroarray(SnpQueryDTO queryDTO);

    /**
     * 根据地区查询SNP数据(不合并)
     */
    Map<String, Object> getSnpDataByRegionMicroarray(SnpQueryDTO queryDTO);



    /**
     * 字符串首字母大写
     */
    String capitalize(String str);
}

