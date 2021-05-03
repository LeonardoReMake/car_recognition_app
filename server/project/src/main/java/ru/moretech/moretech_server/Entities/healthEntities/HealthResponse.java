package ru.moretech.moretech_server.Entities.healthEntities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HealthResponse {
    private static final Logger LOG = LoggerFactory.getLogger(HealthResponse.class);

    private boolean isOk;
    private String errorMsg;

    public HealthResponse() {}

    public HealthResponse(boolean isOk, String errorMsg) {
        this.isOk = isOk;
        this.errorMsg = errorMsg;
    }

    public static HealthResponse success() { return new HealthResponse(true, null); }

    public static HealthResponse fail() { return new HealthResponse(false, null); }

    public static HealthResponse fail(String errorMsg) { return new HealthResponse(false, errorMsg); }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
