package ru.moretech.moretech_server.Entities.RecognitionEntities;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.moretech.moretech_server.Entities.healthEntities.HealthResponse;

import java.util.Map;


public class CarResponse {
    @JsonProperty
    private Map<String, Double> probabilities;
    private boolean confidence = true;
    @JsonProperty
    private HealthResponse health = null;

    public boolean isConfidence() {
        return confidence;
    }

    public void setConfidence(boolean confidence) {
        this.confidence = confidence;
    }

    public Map<String, Double> getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(Map<String, Double> probabilities) {
        this.probabilities = probabilities;
    }

    public HealthResponse getHealth() {
        return health;
    }

    public void setHealth(HealthResponse health) {
        this.health = health;
    }
}
