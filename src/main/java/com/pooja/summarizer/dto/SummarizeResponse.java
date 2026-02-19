package com.pooja.summarizer.dto;

/**
 * Response payload for /api/summarize
 */
public class SummarizeResponse {

    private final String summary;
    private final String mode;

    public SummarizeResponse(String summary, String mode) {
        this.summary = summary;
        this.mode = mode;
    }

    public String getSummary() {
        return summary;
    }

    public String getMode() {
        return mode;
    }
}
