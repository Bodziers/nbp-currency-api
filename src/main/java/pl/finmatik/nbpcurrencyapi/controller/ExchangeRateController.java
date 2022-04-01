package pl.finmatik.nbpcurrencyapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.finmatik.nbpcurrencyapi.domain.Rate;
import pl.finmatik.nbpcurrencyapi.service.CurrencyRestClientService;
import pl.finmatik.nbpcurrencyapi.service.ExchangeRateService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExchangeRateController {

    private final CurrencyRestClientService currencyRestClientService;

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(CurrencyRestClientService currencyRestClientService, ExchangeRateService exchangeRateService) {
        this.currencyRestClientService = currencyRestClientService;
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/table/{date}")
    ResponseEntity<List<Rate>> getRatesForPreviousDay(@PathVariable(value = "date") final String date) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(currencyRestClientService.retrieveCurrencyTable(date));
    }

    @GetMapping("/save/{date}")
    ResponseEntity<List<Rate>> saveRatesForDay(@PathVariable(value = "date") final String date) throws IOException {
        List<Rate> rates = currencyRestClientService.retrieveCurrencyTable(date);
        exchangeRateService.saveCurrencyToCsv(rates, date);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(rates);
    }

    @GetMapping("/calculate/{date}/{fromCurrencyCode}/{toCurrencyCode}/{amount}")
    ResponseEntity<BigDecimal> calculateAmount(
            @PathVariable(value = "date") final String date,
            @PathVariable(value = "fromCurrencyCode") final String fromCurrencyCode,
            @PathVariable(value = "toCurrencyCode") final String toCurrencyCode,
            @PathVariable(value = "amount") final BigDecimal amount) {
        List<Rate> rates = currencyRestClientService.retrieveCurrencyTable(date);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(exchangeRateService.calculateCurrencyRate(rates,fromCurrencyCode,toCurrencyCode,amount));
    }

}
