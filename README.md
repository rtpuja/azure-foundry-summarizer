# Azure AI Foundry Summarizer (Spring Boot)

A small beginner-friendly GenAI project: a REST API that summarizes text using **Azure AI Foundry / Azure OpenAI** Chat Completions.

## Prerequisites
- Java 17+
- Maven 3.9+
- An Azure AI Foundry / Azure OpenAI deployment (example deployment name: `summarizer-api`)

## Configure environment variables

```bash
export AZURE_OPENAI_ENDPOINT="https://landside-ai.cognitiveservices.azure.com"
export AZURE_OPENAI_API_KEY="<YOUR_KEY>"
export AZURE_OPENAI_DEPLOYMENT="summarizer-api"
# optional
export AZURE_OPENAI_API_VERSION="2025-01-01-preview"
```

## Run

```bash
mvn spring-boot:run
```

Health check:
```bash
curl http://localhost:8080/api/health
```

## Summarize API

### Request
`POST /api/summarize`

```json
{
  "text": "paste your long text here...",
  "mode": "BULLETS",
  "maxWords": 120
}
```

Modes: `SHORT`, `BULLETS`, `DETAILED`

### Curl example
```bash
curl -X POST http://localhost:8080/api/summarize   -H "Content-Type: application/json"   -d '{
    "text": "Azure AI Foundry helps you deploy models and build AI apps...",
    "mode": "BULLETS",
    "maxWords": 120
  }'
```

### Response
```json
{
  "summary": "- ...\n- ...",
  "mode": "BULLETS"
}
```

## Notes
- The Azure endpoint base URL is the host only (no path):
  - ✅ `https://landside-ai.cognitiveservices.azure.com`
  - ❌ do not put `/openai/deployments/...` into `AZURE_OPENAI_ENDPOINT`
- The code calls:
  `POST /openai/deployments/{deployment}/chat/completions?api-version={apiVersion}`
