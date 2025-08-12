package big.data.bigdata.utils;

import big.data.bigdata.dto.SnpQueryDTO;
import big.data.bigdata.service.SnpDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SnpQueryHelper {
    @Autowired
    private SnpDataService snpDataService;

    // 修改方法，接受 Excel 转换后的 List<Map<String, Object>> 数据
    public List<Map<String, Object>> getSnpDataIndividualMerge(List<Map<String, Object>> inputList) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> item : inputList) {
            String chr = item.get("chr").toString();
            String pos = item.get("pos").toString();

            // 构造 SnpQueryDTO 用于查询
            SnpQueryDTO queryDTO = new SnpQueryDTO();
            queryDTO.setChromosome(Integer.parseInt(chr));
            queryDTO.setPosition(Integer.parseInt(pos));
            queryDTO.setPage(1);  // 可以调整页码
            queryDTO.setSize(100);  // 每次查询的记录数

            // 调用 SnpDataService 查询数据
            Map<String, Object> snpInfo = snpDataService.getSnpDataIndividualMerge(queryDTO);

            // 合并原始数据和查询结果
            Map<String, Object> merged = new HashMap<>(item);
            if (snpInfo != null) {
                merged.putAll(snpInfo);  // 添加查询结果到原始数据后
            } else {
                merged.put("message", "not found");
            }

            result.add(merged);
        }

        return result;
    }
}
