package work.rabbi.afis.controllers;

import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.UareUException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import work.rabbi.afis.dtos.FingerprintVerifyResponse;
import work.rabbi.afis.services.BackgroundJobService;
import work.rabbi.afis.services.FingerprintService;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class AfisRestController {
    @Autowired
    private JobScheduler jobScheduler;
    @Autowired
    private BackgroundJobService backgroundJobService;

    @GetMapping("/sample")
    public String getSample() {
        return "Hello, OpenAPI!";
    }

    @PostMapping(value = "/verify", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public @ResponseBody String verifyFingerprint(
            @RequestParam(value = "finger", required = true) MultipartFile fingerprint,
            @RequestParam(value = "id", required = true) String id) {
        jobScheduler.enqueue(() -> backgroundJobService.executeSampleJob("Test", fingerprint.getBytes()));

        return "AFIS request for id: " + id + " request accepted!";
    }
}
