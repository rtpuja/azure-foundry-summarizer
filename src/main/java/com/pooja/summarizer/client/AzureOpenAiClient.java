package com.pooja.summarizer.client;

import com.pooja.summarizer.exception.LlmUpstreamException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Azure AI Foundry / Azure OpenAI Chat Completions client.
 *
 * Uses API key authentication and calls:
 * POST {endpoint}/openai/deployments/{deployment}/chat/completions?api-version={apiVersion}
 */
@Component
public class AzureOpenAiClient implements LlmClient {

    private final WebClient webClient;

    @Value("${azure.openai.apiKey}")
    private String apiKey;

    @Value("${azure.openai.deployment}")
    private String deployment;

    @Value("${azure.openai.apiVersion}")
    private String apiVersion;

    @Value("${llm.temperature}")
    private double temperature;

    @Value("${llm.maxTokens}")
    private int maxTokens;

    @Value("${llm.requestTimeoutSeconds}")
    private long timeoutSeconds;

    public AzureOpenAiClient(@Value("${azure.openai.endpoint}") String endpoint) {
        this.webClient = WebClient.builder()
                .baseUrl(endpoint)
                .build();
    }

    @Override
    public String complete(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful assistant that summarizes text."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", temperature,
                "max_tokens", maxTokens
        );

        try {
            Map<?, ?> response = webClient.post()
                    .uri("/openai/deployments/{deployment}/chat/completions?api-version={apiVersion}", deployment, apiVersion)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("api-key", apiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofSeconds(timeoutSeconds))
                    .block();

            if (response == null) {
                throw new LlmUpstreamException("Empty response from Azure OpenAI");
            }

            // Extract choices[0].message.content
            var choices = (List<Map<String, Object>>) response.get("choices");
            if (choices == null || choices.isEmpty()) {
                throw new LlmUpstreamException("No 'choices' returned from Azure OpenAI");
            }

            var first = choices.get(0);
            var message = (Map<String, Object>) first.get("message");
            if (message == null || message.get("content") == null) {
                throw new LlmUpstreamException("No 'message.content' returned from Azure OpenAI");
            }

            return String.valueOf(message.get("content")).trim();
        } catch (WebClientResponseException e) {
            // Includes status code and response body from Azure
            throw new LlmUpstreamException("Azure OpenAI error: " + e.getStatusCode() + " - " + safeBody(e), e);
        } catch (Exception e) {
            throw new LlmUpstreamException("Azure OpenAI call failed: " + e.getMessage(), e);
        }
    }

    private String safeBody(WebClientResponseException e) {
        String body = e.getResponseBodyAsString();
        if (body == null) return "";
        // avoid huge logs
        return body.length() > 2000 ? body.substring(0, 2000) + "..." : body;
    }
}
