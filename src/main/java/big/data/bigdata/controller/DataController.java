package big.data.bigdata.controller;

import big.data.bigdata.annotation.RateLimit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DataController {
    @RateLimit(capacity = 500, tokens = 1, time = 3600) // 1小时500次
    @GetMapping("/data")
    public ResponseEntity<?> getData() {
        return ResponseEntity.ok("Data content");
    }
}
