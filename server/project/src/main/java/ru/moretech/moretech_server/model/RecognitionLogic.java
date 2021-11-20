package ru.moretech.moretech_server.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.moretech.moretech_server.Entities.RecognitionEntities.CarResponse;
import ru.moretech.moretech_server.Entities.RecognitionEntities.CarSuggestionsResponse;
import ru.moretech.moretech_server.Entities.RecognitionEntities.Content;
import ru.moretech.moretech_server.Entities.clientEntities.Car;
import ru.moretech.moretech_server.Entities.healthEntities.HealthResponse;
import ru.moretech.moretech_server.api.MLServerApi;
import ru.moretech.moretech_server.datasource.CarDatasource;

import java.util.ArrayList;
import java.util.List;

@Component
public class RecognitionLogic {
    private static final Logger LOG = LoggerFactory.getLogger(RecognitionLogic.class);

    @Autowired
    private MLServerApi recognitionApi;

    @Autowired
    private CarDatasource carDatasource;

    public HealthResponse checkRecognitionServer() {
        try {
            return recognitionApi.checkHealth();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return HealthResponse.fail("Internal error, while parsing response from recognition server. Try to connect later.");
        }
    }

    public CarResponse recognizeExtended(Content content) {
        HealthResponse healthResponse = checkRecognitionServer();
        if (healthResponse.isOk()) {
            try {
                CarResponse carResponse = recognitionApi.getCarResponse(content);
                carResponse.setHealth(healthResponse);
                return carResponse;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                CarResponse carResponse = new CarResponse();
                carResponse.setProbabilities(null);
                carResponse.setHealth(healthResponse);
                return carResponse;
            }
        } else {
            CarResponse carResponse = new CarResponse();
            carResponse.setHealth(healthResponse);
            return carResponse;
        }
    }

    public CarSuggestionsResponse suggest(Content content) {
        CarSuggestionsResponse carSuggestionsResponse = new CarSuggestionsResponse();
        HealthResponse healthResponse = checkRecognitionServer();
        carSuggestionsResponse.setHealthResponse(healthResponse);

        if (!healthResponse.isOk()) return carSuggestionsResponse;

        String[] suggestedCars;
        try {
            suggestedCars = recognitionApi.getCarSuggestion(content);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        List<Car> listedCars = new ArrayList<>();
        List<Car> allCars = carDatasource.getAllCars();

        for (String suggestedCar : suggestedCars) {
            for (Car car : allCars) {
                String marketplaceBrandModel =
                        (car.getCarBrand() +
                                " " +
                                car.getTitle()).toLowerCase();

                if (marketplaceBrandModel.equals(suggestedCar.toLowerCase())) {
                    ArrayList<String> photos = new ArrayList<>();
                    photos.add(car.getPhoto());
                    photos.addAll(car.getPhotos());

                    listedCars.add(new Car(
                            car.getCarBrand(),
                            car.getMinprice(),
                            car.getPhoto(),
                            car.getTitle(),
                            car.getTitleRus(),
                            photos
                    ));
                }
            }
        }

        carSuggestionsResponse.setCars(listedCars);
        return carSuggestionsResponse;
    }
}
