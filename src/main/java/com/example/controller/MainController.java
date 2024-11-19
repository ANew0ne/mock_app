package com.example.controller;

import com.example.model.RequestDTO;
import com.example.model.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@RestController
public class MainController {
    private Logger log = LoggerFactory.getLogger(MainController.class);
    ObjectMapper mapper = new ObjectMapper();
    private Random random = new Random();

    @PostMapping(
            value = "/info/postBalances",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Object postBalances(@RequestBody RequestDTO requestDTO) {
        try {
            String clientId = requestDTO.getClientID();
            char firstDigit = clientId.charAt(0);
            BigDecimal maxLimit;
            String RqUID = requestDTO.getRqUID();
            String currency;

            if (firstDigit == '8') {
                maxLimit = new BigDecimal(2000).setScale(2, RoundingMode.HALF_UP);;
                currency = "US";
            } else if (firstDigit == '9') {
                maxLimit = new BigDecimal(1000).setScale(2, RoundingMode.HALF_UP);;
                currency = "EU";
            } else {
                maxLimit = new BigDecimal(10000).setScale(2, RoundingMode.HALF_UP);;
                currency = "RUB";
            }

            BigDecimal balance = generateRandomBalance(maxLimit);

            ResponseDTO responseDTO = new ResponseDTO();

            responseDTO.setRqUID(RqUID);
            responseDTO.setClientID(clientId);
            responseDTO.setAccount(requestDTO.getAccount());
            responseDTO.setCurrency(currency);
            responseDTO.setBalance(balance);
            responseDTO.setMaxLimit(maxLimit);

            log.error("********** RequestDTO **********" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));
            log.error("********** ResponceDTO **********" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO));

            return responseDTO;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    private BigDecimal generateRandomBalance(BigDecimal maxLimit) {
        double randomValue = random.nextDouble() * maxLimit.doubleValue();
        return BigDecimal.valueOf(randomValue).setScale(2, RoundingMode.HALF_UP);
    }
}
