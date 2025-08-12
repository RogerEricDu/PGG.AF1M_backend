package big.data.bigdata.controller;

import big.data.bigdata.dto.SnpQueryDTO;
import big.data.bigdata.entity.SnpData;
import big.data.bigdata.service.SnpDataService;
import big.data.bigdata.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/select")
public class SnpDataController {
    @Autowired
    private SnpDataService snpDataService;

    @PostMapping("/NGS/individualMergeSNP")
    public ResponseEntity<Map<String, Object>> getSnpDataIndividualMerge(@RequestBody SnpQueryDTO queryDTO) {
        Map<String, Object> result = snpDataService.getSnpDataIndividualMerge(queryDTO);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/NGS/individualSNP")
    public ResponseEntity<Map<String, Object>> getSnpData(@RequestBody SnpQueryDTO queryDTO) {
        Map<String, Object> result = snpDataService.getSnpData(queryDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/NGS/provinceMergeSNP")
    public ResponseEntity<Map<String, Object>> getSnpDataByProvinceMerge(@RequestBody SnpQueryDTO queryDTO){
        Map<String, Object> result = snpDataService.getSnpDataByProvinceMerge(queryDTO);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/NGS/provinceSNP")
    public ResponseEntity<Map<String, Object>> getSnpDataByProvince(@RequestBody SnpQueryDTO queryDTO){
        Map<String, Object> result = snpDataService.getSnpDataByProvince(queryDTO);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/NGS/regionMergeSNP")
    public ResponseEntity<Map<String, Object>> getSnpDataByRegionMerge(@RequestBody SnpQueryDTO queryDTO){
        Map<String, Object> result = snpDataService.getSnpDataByRegionMerge(queryDTO);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/NGS/regionSNP")
    public ResponseEntity<Map<String, Object>> getSnpDataByRegion(@RequestBody SnpQueryDTO queryDTO){
        Map<String, Object> result = snpDataService.getSnpDataByRegion(queryDTO);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/Microarray/individualMergeSNP")
    public ResponseEntity<Map<String, Object>> getSnpDataIndividualMergeMicroarray(@RequestBody SnpQueryDTO queryDTO) {
        Map<String, Object> result = snpDataService.getSnpDataIndividualMergeMicroarray(queryDTO);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/Microarray/individualSNP")
    public ResponseEntity<Map<String, Object>> getSnpDataMicroarray(@RequestBody SnpQueryDTO queryDTO) {
        Map<String, Object> result = snpDataService.getSnpDataMicroarray(queryDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/Microarray/provinceMergeSNP")
    public ResponseEntity<Map<String, Object>> getSnpDataByProvinceMergeMicroarray(@RequestBody SnpQueryDTO queryDTO){
        Map<String, Object> result = snpDataService.getSnpDataByProvinceMergeMicroarray(queryDTO);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/Microarray/provinceSNP")
    public ResponseEntity<Map<String, Object>> getSnpDataByProvinceMicroarray(@RequestBody SnpQueryDTO queryDTO){
        Map<String, Object> result = snpDataService.getSnpDataByProvinceMicroarray(queryDTO);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/Microarray/regionMergeSNP")
    public ResponseEntity<Map<String, Object>> getSnpDataByRegionMergeMicroarray(@RequestBody SnpQueryDTO queryDTO){
        Map<String, Object> result = snpDataService.getSnpDataByRegionMergeMicroarray(queryDTO);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/Microarray/regionSNP")
    public ResponseEntity<Map<String, Object>> getSnpDataByRegionMicroarray(@RequestBody SnpQueryDTO queryDTO){
        Map<String, Object> result = snpDataService.getSnpDataByRegionMicroarray(queryDTO);
        return ResponseEntity.ok(result);
    }

/*    @PostMapping("/populationMergeSNP")
    public ResponseEntity<Map<String, Object>> getSnpDataByPopulationMerge(@RequestBody SnpQueryDTO queryDTO){
        Map<String, Object> result = snpDataService.getSnpDataByPopulationMerge(queryDTO);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/populationSNP")
    public ResponseEntity<Map<String, Object>> getSnpDataByPopulation(@RequestBody SnpQueryDTO queryDTO){
        Map<String, Object> result = snpDataService.getSnpDataByPopulation(queryDTO);
        return ResponseEntity.ok(result);
    }*/

}


