package big.data.bigdata.controller;

import big.data.bigdata.entity.VepRequest;
import big.data.bigdata.entity.VepResponse;
import big.data.bigdata.service.VepService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vep")
@RequiredArgsConstructor

public class VepController {
/*    private final VepService vepService;

    @PostMapping("/annotate")
    public ResponseEntity<VepResponse> annotate(
            @Valid @RequestBody VepRequest request,
            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid SNP parameters");
        }
        return ResponseEntity.ok(vepService.annotate(request));
    }*/
}
