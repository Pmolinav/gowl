package com.pmolinav;

import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ResponseResults {
    private final ClientHttpResponse response;
    private int status;
    private final String body;
    private final Map<String, String> headers;

    public ResponseResults(ClientHttpResponse response, int status, String body, Map<String, String> headers) {
        this.response = response;
        this.status = status;
        this.body = body;
        this.headers = headers;
    }

    public ResponseResults(final ClientHttpResponse response) throws IOException {
        this.response = response;
        this.status = response.getStatusCode().value();
        this.body = extractBodyAsString(response);
        Map<String, String> parsedHeaders = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : response.getHeaders().entrySet()) {
            parsedHeaders.put(entry.getKey(), String.join(",", entry.getValue()));
        }
        this.headers = parsedHeaders;
    }

    public static String extractBodyAsString(ClientHttpResponse response) throws IOException {
        byte[] bodyBytes = org.springframework.util.StreamUtils.copyToByteArray(response.getBody());
        return new String(bodyBytes, StandardCharsets.UTF_8);
    }

    public ClientHttpResponse getResponse() {
        return response;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String key) {
        if (headers == null) {
            return null;
        } else {
            return headers.get(key);
        }
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseResults that = (ResponseResults) o;
        return status == that.status
                && Objects.equals(response, that.response)
                && Objects.equals(body, that.body)
                && Objects.equals(headers, that.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(response, status, body, headers);
    }

    @Override
    public String toString() {
        return "ResponseResults{" +
                "response=" + response +
                ", status=" + status +
                ", body='" + body + '\'' +
                ", headers=" + headers +
                '}';
    }
}