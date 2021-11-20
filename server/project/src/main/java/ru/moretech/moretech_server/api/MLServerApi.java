package ru.moretech.moretech_server.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.moretech.moretech_server.Entities.RecognitionEntities.CarResponse;
import ru.moretech.moretech_server.Entities.RecognitionEntities.Content;
import ru.moretech.moretech_server.Entities.healthEntities.HealthResponse;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MLServerApi {
    private Logger logger = Logger.getLogger(MLServerApi.class.getName());
    @Value("${mlServerUrl}")
    private String mlServerUrl;

    public HealthResponse checkHealth() throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Content> entity = new HttpEntity<>(httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = null;
        try {
            exchange =
                    restTemplate.exchange(mlServerUrl + "/health", HttpMethod.GET, entity, String.class);
        } catch (Exception e) {
            return HealthResponse.fail("Unknown recognition server host. Try to connect later");
        }

        if (exchange.getStatusCode() != HttpStatus.OK) {
            if (exchange.getStatusCode().is5xxServerError()) {
                return HealthResponse.fail("Recognition server does not response. Try to connect later");
            } else {
                return HealthResponse.fail("Unknown error");
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> response = (Map<String, String>) objectMapper.readValue(exchange.getBody(), Map.class);
        if (response.containsKey("status") && response.get("status").equals("ok")) {
            return HealthResponse.success();
        } else {
            return HealthResponse.fail();
        }
    }

    public CarResponse getCarResponse(Content content) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Content> entity = new HttpEntity<>(content, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange =
                restTemplate.exchange(mlServerUrl + "/recognitionExtended", HttpMethod.POST, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(exchange.getBody(), CarResponse.class);
    }

    public String[] getCarSuggestion(Content content) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Content> entity = new HttpEntity<>(content, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange =
                restTemplate.exchange(mlServerUrl + "/suggestion", HttpMethod.POST, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(exchange.getBody(), String[].class);
    }
}
