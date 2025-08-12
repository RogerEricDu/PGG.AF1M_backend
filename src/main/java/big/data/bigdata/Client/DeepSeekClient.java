package big.data.bigdata.Client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DeepSeekClient {

    @Value("${deepseek.base-url}")
    private String apiUrl;

    @Value("${deepseek.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String DEFAULT_MODEL = "Pro/deepseek-ai/DeepSeek-V3";

    public String generateInterpretation(String prompt,boolean requireJson) {
        String requestBody = buildRequestBody(prompt, requireJson);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);
            return parseResponse(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("DeepSeek API call failed: " + e.getMessage());
        }
    }

    private String buildRequestBody(String prompt , boolean requireJson) {
        try {
            ObjectNode root = objectMapper.createObjectNode();
            root.put("model", DEFAULT_MODEL);
            root.put("stream", false);
            root.put("max_tokens", 512);
            root.put("temperature", 0.3);
            root.put("top_p", 0.7);
            root.put("top_k", 50);
            root.put("frequency_penalty", 0.5);
            root.put("n", 1);


            // 动态设置 response_format
            if (requireJson) {
                ObjectNode responseFormat = objectMapper.createObjectNode();
                responseFormat.put("type", "json_object");
                root.set("response_format", responseFormat);
            }

            ArrayNode messages = objectMapper.createArrayNode();
            ObjectNode message = objectMapper.createObjectNode();
            message.put("role", "user");
            message.put("content", prompt);
            messages.add(message);
            root.set("messages", messages);

            return objectMapper.writeValueAsString(root);

        } catch (Exception e) {
            throw new RuntimeException("构建请求体失败", e);
        }
    }

    private String parseResponse(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            return root.get("choices").get(0).get("message").get("content").asText();
        } catch (Exception e) {
            throw new RuntimeException("响应解析失败: " + json);
        }
    }
}