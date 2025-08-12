package big.data.bigdata.service;

import big.data.bigdata.dto.GlobalSnpQueryDTO;
import big.data.bigdata.dto.SnpQueryDTO;

import java.util.Map;

public interface FurtherInfoService {
    Map<String, Object> getVariantEffect(SnpQueryDTO queryDTO);
    Map<String, Object> getProvincialPopulation(SnpQueryDTO queryDTO);
    Map<String, Object> getGeneticSubgroups(SnpQueryDTO queryDTO);
    Map<String, Object> getGlobalMap(GlobalSnpQueryDTO globalSnpQueryDTO);
}
