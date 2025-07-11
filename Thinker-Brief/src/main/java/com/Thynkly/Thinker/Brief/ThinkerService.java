package com.Thynkly.Thinker.Brief;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class ThinkerService {

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public ThinkerService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public String processContent(ThinkerRequest request) {
        // Build the input prompt based on user operation
        String prompt = buildPrompt(request);

        // Construct the Gemini API request body using the correct format
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                ),
                "generationConfig", Map.of(
                        "temperature", 0.2,
                        "topK", 40,
                        "topP", 0.95
                )
        );


        // Construct the full URL with API key as query parameter
        String fullUrl = geminiApiUrl + "?key=" + geminiApiKey;

        try {
            // Send request to Gemini API using WebClient
            String response = webClient.post()
                    .uri(fullUrl)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Parse and return the text output from Gemini response
            return extractTextFromResponse(response);

        } catch (WebClientResponseException e) {
            // Log the error details if needed
            System.err.println("Gemini API error: " + e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            // Fallback for other exceptions
            return "Error calling Gemini API: " + e.getMessage();
        }
    }

    private String extractTextFromResponse(String response) {
        try {
            GeminiResponse geminiResponse = objectMapper.readValue(response, GeminiResponse.class);
            if (geminiResponse.getCandidates() != null && !geminiResponse.getCandidates().isEmpty()) {
                GeminiResponse.Candidate firstCandidate = geminiResponse.getCandidates().get(0);
                if (firstCandidate.getContent() != null &&
                        firstCandidate.getContent().getParts() != null &&
                        !firstCandidate.getContent().getParts().isEmpty()) {
                    return firstCandidate.getContent().getParts().get(0).getText();
                }
            }
            return "No content found in Gemini response.";
        } catch (Exception e) {
            return "Error parsing Gemini response: " + e.getMessage();
        }
    }

    private String buildPrompt(ThinkerRequest request) {
        StringBuilder prompt = new StringBuilder();
        switch (request.getOperation()) {
            case "summarize" -> prompt.append("Provide a clear and concise summary of the following text:\n\n");
            case "suggest" -> prompt.append("Suggest related topics and further reading based on the following text:\n\n");
            default -> throw new IllegalArgumentException("Unknown operation: " + request.getOperation());
        }
        prompt.append(request.getContent());
        return prompt.toString();
    }
}
