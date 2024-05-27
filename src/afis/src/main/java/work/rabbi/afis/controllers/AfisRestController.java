package work.rabbi.afis.controllers;

import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.UareUException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import work.rabbi.afis.dtos.FingerprintVerifyResponse;
import work.rabbi.afis.services.FingerprintService;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class AfisRestController {
    @GetMapping("/sample")
    public String getSample() {
        return "Hello, OpenAPI!";
    }

    @PostMapping(value="/verify", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public @ResponseBody FingerprintVerifyResponse verifyFingerprint(@RequestParam(value = "finger", required = true) MultipartFile fingerprint) throws IOException, UareUException {
        Fmd fmd1 = FingerprintService.generateFmdFromBytes(fingerprint.getBytes());
        return new FingerprintVerifyResponse("OK");
    }
}
