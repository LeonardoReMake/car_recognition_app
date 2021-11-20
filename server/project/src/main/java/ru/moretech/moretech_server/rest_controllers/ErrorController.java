package ru.moretech.moretech_server.rest_controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.moretech.moretech_server.Entities.healthEntities.HealthResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@RestController
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    private static final Logger LOG = LoggerFactory.getLogger(ErrorController.class);

    @RequestMapping("/error")
    public HealthResponse error(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return HealthResponse.fail("404 error. Undefined resource");
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return HealthResponse.fail("500 error. Internal error, check /health request or try to connect later");
            }
        }
        if (status != null) {
            return HealthResponse.fail(status.toString() + " error. Try to connect later");
        } else {
            return HealthResponse.fail("Unknown error. Try to connect later");
        }
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
