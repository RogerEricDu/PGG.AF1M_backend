package big.data.bigdata.service;

import big.data.bigdata.entity.VepRequest;
import big.data.bigdata.entity.VepResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class VepService {
/*    @Value("${vep.docker.url:http://localhost:5000}")
    private String vepUrl;

    public VepResponse annotate(VepRequest request){
        // 1. 构建VEP兼容的HGVS格式
        String hgvs = String.format("%s:%d %s>%s",
                request.getChrom(), request.getPosition(), request.getRef(), request.getAlt());

        // 2. 调用Docker-VEP的REST API
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody = String.format("{\"hgvs\": [\"%s\"]}", hgvs);

        ResponseEntity<String> response = new RestTemplate().exchange(
                vepUrl + "/vep/human/hgvs",
                HttpMethod.POST,
                new HttpEntity<>(requestBody, headers),
                String.class
        );

        // 3. 解析VEP原始JSON响应
        return parseVepResponse(response.getBody());
    }
    private VepResponse parseVepResponse(String json) throws JsonProcessingException {
        JsonNode root = new ObjectMapper().readTree(json);
        JsonNode firstResult = root.get(0);

        VepResponse result = new VepResponse();
        result.setVariantId(firstResult.path("id").asText());
        result.setGene(firstResult.path("gene_symbol").asText());
        result.setConsequence(String.join(",",
                firstResult.path("consequence_terms").findValuesAsText("consequence")));
        result.setHgvsC(firstResult.path("hgvs_c").asText());
        result.setHgvsP(firstResult.path("hgvs_p").asText());

        return result;
    }*/
}
