package ru.moretech.moretech_server.model;

import ru.moretech.moretech_server.Entities.calculatorEntities.calculate.Cost;
import ru.moretech.moretech_server.Entities.calculatorEntities.calculate.InitialFee;
import ru.moretech.moretech_server.Entities.calculatorEntities.calculate.Ranges;
import ru.moretech.moretech_server.Entities.calculatorEntities.calculate.Result;
import ru.moretech.moretech_server.datasource.CalculatorDatasource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.moretech.moretech_server.Entities.calculatorEntities.calculate.CalculateRequest;
import ru.moretech.moretech_server.Entities.calculatorEntities.calculate.CalculateResponse;
import ru.moretech.moretech_server.Entities.calculatorEntities.settings.SettingsResponse;

@Component
public class Calculator {
    private static final Logger LOG = LoggerFactory.getLogger(Calculator.class);

    private final CalculatorDatasource calculatorDatasource;

    @Autowired
    public Calculator(CalculatorDatasource calculatorDatasource) {
        this.calculatorDatasource = calculatorDatasource;
    }

    public SettingsResponse receiveSettings(String name, String lang) {
        calculatorDatasource.saveSettings(name, lang);
        return new SettingsResponse();
    }

    public CalculateResponse calculate(CalculateRequest calculateRequest) {
        double loanAmount = calculateRequest.getCost();
        double initialFee = calculateRequest.getInitialFee();
        double residualPayment = calculateRequest.getResidualPayment();
        int kaskoValue = calculateRequest.getKaskoValue();
        CalculateResponse calculateResponse = new CalculateResponse();

        Ranges ranges = new Ranges();
        ranges.setCost(new Cost(true, (int) Math.round(loanAmount), (int) Math.round(loanAmount)));
        ranges.setInitialFee(new InitialFee(true, (int) Math.round(initialFee),(int) Math.round(initialFee)));
        calculateResponse.setRanges(ranges);
        calculateResponse.setResult(calc(loanAmount, initialFee, residualPayment, kaskoValue));
        return calculateResponse;
    }

    private Result calc(double loanAmount, double initialFee, double residualPayment, int kaskoValue) {
        Result result = new Result();
        result.setKaskoCost(kaskoValue*2000);
        result.setLastPayment(residualPayment);
        result.setLoanAmount(loanAmount+loanAmount*0.16*kaskoValue);
        result.setTerm(kaskoValue);
        return result;
    }

}
