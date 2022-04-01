package pl.finmatik.nbpcurrencyapi.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.finmatik.nbpcurrencyapi.domain.Rate;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExchangeRateServiceTest {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Test
    void saveCurrencyToCsv() throws IOException {
        List<Rate> rates = new ArrayList<>();
        File file = new File("src/main/resources/static/1111-01-01_currencies_exchange_rate.csv");
        rates.add(new Rate("Dollar", "USD", 0.04f));
        rates.add(new Rate("Kuna", "CHR", 0.02f));
        exchangeRateService.saveCurrencyToCsv(rates,"1111-01-01");
        Assertions.assertTrue(file.exists());
        file.delete();
    }

    @Test
    void calculateCurrencyRateFromCurrencyOtherThenPLN() {
        List<Rate> rates = new ArrayList<>();
        rates.add(new Rate("Dollar", "USD", 4.1801f));
        rates.add(new Rate("Frank", "CHF", 4.5207f));
        rates.add(new Rate("Funt", "GBP", 5.4842f));
        BigDecimal result = exchangeRateService.calculateCurrencyRate(rates,"USD","PLN", new BigDecimal("1000.00"));
        Assertions.assertEquals(new BigDecimal("4180.10"), result);
    }
    @Test
    void calculateCurrencyRateFromPLNToCurrency() {
        List<Rate> rates = new ArrayList<>();
        rates.add(new Rate("Dollar", "USD", 4.1801f));
        rates.add(new Rate("Frank", "CHF", 4.5207f));
        rates.add(new Rate("Funt", "GBP", 5.4842f));
        BigDecimal result = exchangeRateService.calculateCurrencyRate(rates,"PLN","USD", new BigDecimal("1000.00"));
        Assertions.assertEquals(new BigDecimal("239.23"), result);
    }
    @Test
    void calculateCurrencyRateFromPLNToPLN() {
        List<Rate> rates = new ArrayList<>();
        rates.add(new Rate("Dollar", "USD", 4.1801f));
        rates.add(new Rate("Frank", "CHF", 4.5207f));
        rates.add(new Rate("Funt", "GBP", 5.4842f));
        BigDecimal result = exchangeRateService.calculateCurrencyRate(rates,"PLN","PLN", new BigDecimal("1000.00"));
        Assertions.assertEquals(new BigDecimal("1000.00"), result);
    }
    @Test
    void calculateCurrencyRateFromCurrencyToCurrency() {
        List<Rate> rates = new ArrayList<>();
        rates.add(new Rate("Dollar", "USD", 4.1801f));
        rates.add(new Rate("Frank", "CHF", 4.5207f));
        rates.add(new Rate("Funt", "GBP", 5.4842f));
        BigDecimal result = exchangeRateService.calculateCurrencyRate(rates,"CHF","USD", new BigDecimal("1000.00"));
        Assertions.assertEquals(new BigDecimal("1081.48"), result);
    }
    @Test
    void calculateCurrencyRateSameCurrency() {
        List<Rate> rates = new ArrayList<>();
        rates.add(new Rate("Dollar", "USD", 4.1801f));
        rates.add(new Rate("Frank", "CHF", 4.5207f));
        rates.add(new Rate("Funt", "GBP", 5.4842f));
        BigDecimal result = exchangeRateService.calculateCurrencyRate(rates,"CHF","CHF", new BigDecimal("1000.00"));
        Assertions.assertEquals(new BigDecimal("1000.00"), result);
    }

}
