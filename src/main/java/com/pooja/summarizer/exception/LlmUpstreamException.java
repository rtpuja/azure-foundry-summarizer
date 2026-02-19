package com.pooja.summarizer.exception;

public class LlmUpstreamException extends RuntimeException {
    public LlmUpstreamException(String message) {
        super(message);
    }

    public LlmUpstreamException(String message, Throwable cause) {
        super(message, cause);
    }
}
