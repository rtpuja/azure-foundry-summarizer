package com.pooja.summarizer.service;

import com.pooja.summarizer.client.LlmClient;
import com.pooja.summarizer.dto.SummarizeRequest;
import org.springframework.stereotype.Service;

@Service
public class SummarizeService {

    private final LlmClient llmClient;

    public SummarizeService(LlmClient llmClient) {
        this.llmClient = llmClient;
    }

    public String summarize(SummarizeRequest req) {
        String instruction = switch (req.getMode()) {
            case SHORT -> "Summarize in a short paragraph.";
            case BULLETS -> "Summarize in 5-7 bullet points.";
            case DETAILED -> "Summarize with key points and a short conclusion.";
        };

        int maxWords = (req.getMaxWords() == null) ? 120 : req.getMaxWords();

        String prompt =
                "Follow these rules strictly:\n" +
                "1) " + instruction + "\n" +
                "2) Keep it under " + maxWords + " words.\n" +
                "3) Do not add facts not present in the text.\n\n" +
                "TEXT:\n" +
                req.getText();

        return llmClient.complete(prompt);
    }
}
