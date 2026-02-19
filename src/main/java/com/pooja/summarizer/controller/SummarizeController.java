package com.pooja.summarizer.controller;

import com.pooja.summarizer.dto.SummarizeRequest;
import com.pooja.summarizer.dto.SummarizeResponse;
import com.pooja.summarizer.service.SummarizeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SummarizeController {

    private final SummarizeService summarizeService;

    public SummarizeController(SummarizeService summarizeService) {
        this.summarizeService = summarizeService;
    }

    @PostMapping("/summarize")
    public SummarizeResponse summarize(@Valid @RequestBody SummarizeRequest request) {
        String summary = summarizeService.summarize(request);
        return new SummarizeResponse(summary, request.getMode().name());
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
