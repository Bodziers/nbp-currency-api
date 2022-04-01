package pl.finmatik.nbpcurrencyapi.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import pl.finmatik.nbpcurrencyapi.domain.Rate;
import pl.finmatik.nbpcurrencyapi.domain.RateHeadersEnum;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ExchangeRateService {

    private static final Logger log = getLogger(ExchangeRateService.class);

    public void saveCurrencyToCsv(List<Rate> rates, String date) throws IOException {

        int maxTries = 3;
        for (int i = 0; i < maxTries;i++) {
            FileWriter fileWriter = new FileWriter("src/main/resources/static/" + date + "_currencies_exchange_rate.csv");
            try (CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT)) {
                csvPrinter.printRecord(RateHeadersEnum.NAME, RateHeadersEnum.CODE, RateHeadersEnum.EXCHANGE_RATE);
                for (Rate rate : rates
                ) {
                    csvPrinter.printRecord(rate.getCurrency(), rate.getCode(), rate.getMid());
                }
            } catch (IOException e) {
                log.error("Error While writing CSV, attemp :" + i + " from 3", e);
                new File("src/main/resources/static/" + date + "_currencies_exchange_rate.csv");
            }
        }
    }

    public BigDecimal calculateCurrencyRate(
            List<Rate> rates,
            String fromCurrencyCode,
            String toCurrencyCode,
            BigDecimal amount) {

        //Adding PLN currency with rate set to 1 to allow simple calculation of exchange rates
        rates.add(new Rate("Zloty","PLN",1.00f));

        Rate fromCurrency = rates.stream()
                .filter(rate1 -> fromCurrencyCode.equals(rate1.getCode()))
                .findAny()
                .orElse(null);

        Rate toCurrency = rates.stream()
                .filter(rate2 -> toCurrencyCode.equals(rate2.getCode()))
                .findAny()
                .orElse(null);

        BigDecimal calculatedAmount = (amount.multiply(BigDecimal.valueOf(1/toCurrency.getMid())).multiply(BigDecimal.valueOf(fromCurrency.getMid())));
        return calculatedAmount.setScale(2, RoundingMode.HALF_UP);
    }
}
