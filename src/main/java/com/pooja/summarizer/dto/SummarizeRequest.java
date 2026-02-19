package com.pooja.summarizer.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request payload for /api/summarize
 */
public class SummarizeRequest {

    @NotBlank(message = "text must not be blank")
    private String text;

    @NotNull(message = "mode must not be null")
    private Mode mode = Mode.SHORT;

    @Min(value = 30, message = "maxWords must be at least 30")
    @Max(value = 400, message = "maxWords must be at most 400")
    private Integer maxWords = 120;

    public enum Mode {
        SHORT,
        BULLETS,
        DETAILED
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Integer getMaxWords() {
        return maxWords;
    }

    public void setMaxWords(Integer maxWords) {
        this.maxWords = maxWords;
    }
}
