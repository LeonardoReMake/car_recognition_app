package ru.moretech.moretech_server.Entities.RecognitionEntities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.moretech.moretech_server.Entities.clientEntities.Car;
import ru.moretech.moretech_server.Entities.healthEntities.HealthResponse;

import java.util.List;

public class CarSuggestionsResponse {
    private static final Logger LOG = LoggerFactory.getLogger(CarSuggestionsResponse.class);

    @JsonProperty
    private List<Car> cars;
    @JsonProperty
    private HealthResponse healthResponse;

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public HealthResponse getHealthResponse() {
        return healthResponse;
    }

    public void setHealthResponse(HealthResponse healthResponse) {
        this.healthResponse = healthResponse;
    }
}
