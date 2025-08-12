package big.data.bigdata.controller;

import big.data.bigdata.dto.GlobalSnpQueryDTO;
import big.data.bigdata.dto.SnpQueryDTO;
import big.data.bigdata.service.FurtherInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/furtherInfo")
public class FurtherInfoController {
    @Autowired
    private FurtherInfoService furtherInfoService;

    @PostMapping("/provincialPopulation")
    public ResponseEntity<Map<String,Object>> getProvincialPopulation(@RequestBody SnpQueryDTO queryDTO){
        Map<String,Object> result = furtherInfoService.getProvincialPopulation(queryDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/geneticSubgroups")
    public ResponseEntity<Map<String ,Object>> getGeneticSubgroups (@RequestBody SnpQueryDTO queryDTO){
        Map<String,Object> result = furtherInfoService.getGeneticSubgroups(queryDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/globalMap")
    public ResponseEntity<Map<String,Object>> getGlobalMap(@RequestBody GlobalSnpQueryDTO globalSnpQueryDTO){
        Map<String,Object> result = furtherInfoService.getGlobalMap(globalSnpQueryDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/vep")
    public ResponseEntity<Map<String, Object>> getVariantEffect(@RequestBody SnpQueryDTO queryDTO) {
        Map<String, Object> result = furtherInfoService.getVariantEffect(queryDTO);
        return ResponseEntity.ok(result);
    }
}
