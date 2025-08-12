package big.data.bigdata.controller;

import big.data.bigdata.Client.DeepSeekClient;
import big.data.bigdata.Prompt.DeepSeekPrompt;
import big.data.bigdata.dto.*;
import big.data.bigdata.service.DeepSeekService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/DeepSeek")
public class DeepSeekController {

    private final DeepSeekClient deepSeekClient;

    public DeepSeekController(DeepSeekClient deepSeekClient) {
        this.deepSeekClient = deepSeekClient;
    }

    @PostMapping("/annotateSnp")
    public ResponseEntity<SNPAnnotationResult> annotateSNP(@RequestBody SNPAnnotationRequest request) {
        // 构建基因组位置标识
        String genomicLocation = request.getChromosome() + ":" + request.getPosition();

        // 添加可选字段说明
        StringBuilder inputBuilder = new StringBuilder();
        inputBuilder.append("Genomic Location: ").append(genomicLocation);
        if (request.getReferenceAllele() != null && request.getAlternateAllele() != null) {
            inputBuilder.append("\nAlleles: ").append(request.getReferenceAllele())
                    .append(">").append(request.getAlternateAllele());
        }

        String fullPrompt = DeepSeekPrompt.SNP_ANNOTATION.getPrompt()
                + "\nInput SNP Characteristics:\n" + inputBuilder;

        String response = deepSeekClient.generateInterpretation(fullPrompt,true); //需要输出json格式
        return ResponseEntity.ok(parseSNPResult(response));
    }

    @PostMapping("/annotateGene")
    public ResponseEntity<GeneAnnotationResult> annotateGene(@RequestBody GeneAnnotationRequest request) {
        String fullPrompt = DeepSeekPrompt.GENE_ANNOTATION.getPrompt() +
                "\nGene symbol: " + request.getGeneSymbol();
        String response = deepSeekClient.generateInterpretation(fullPrompt,true);//需要输出json格式
        return ResponseEntity.ok(parseGeneResult(response));
    }

    @PostMapping("/ask")
    public ResponseEntity<Map<String, Object>> askQuestion(@RequestBody QuestionRequest request) {
        // Word count check
        if (request.getQuestion().split("\\s+").length > 40) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 400);
            errorResponse.put("answer", "Question exceeds 40 words limit.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        String fullPrompt = DeepSeekPrompt.QNA_ASSISTANT.getPrompt() +
                "Question: " + request.getQuestion();

        try {
            String response = deepSeekClient.generateInterpretation(fullPrompt,false);//输出text格式

            // Parse the JSON response from DeepSeek to extract the answer text
            String answerText;
            try {
                JsonNode jsonNode = new ObjectMapper().readTree(response);
                answerText = jsonNode.get("answer").asText(); // Adjust this based on the actual JSON structure
            } catch (Exception e) {
                // If parsing fails, use the raw response as answer
                answerText = response;
            }

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("code", 200);
            successResponse.put("answer", answerText);

            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("answer", "Failed to generate answer: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    private SNPAnnotationResult parseSNPResult(String jsonResponse) {
        try {
            return new ObjectMapper().readValue(jsonResponse, SNPAnnotationResult.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse SNP annotation result", e);
        }
    }

    private GeneAnnotationResult parseGeneResult(String jsonResponse) {
        try {
            return new ObjectMapper().readValue(jsonResponse, GeneAnnotationResult.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse gene annotation result", e);
        }
    }
}