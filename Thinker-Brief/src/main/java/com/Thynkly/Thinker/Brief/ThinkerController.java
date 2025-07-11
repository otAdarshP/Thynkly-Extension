package com.Thynkly.Thinker.Brief;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestController
@RequestMapping("/api/thinker")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class ThinkerController {
    private final ThinkerService thinkerService;

    @PostMapping("/process")
    public ResponseEntity<String> processContent(@RequestBody ThinkerRequest request) {
        try {
            String result = thinkerService.processContent(request);
            return ResponseEntity.ok(result);
        } catch (WebClientResponseException e) {
            // log the real error body for troubleshooting
            String errorBody = e.getResponseBodyAsString();
            // e.g. log.error("Gemini error: {}", errorBody);
            return ResponseEntity
                    .status(HttpStatus.BAD_GATEWAY)
                    .body("Gemini API error: "
                            + e.getStatusCode()
                            + " — "
                            + (errorBody.length() > 200 ? errorBody.substring(0,200) + "…" : errorBody));
        }
    }
}
