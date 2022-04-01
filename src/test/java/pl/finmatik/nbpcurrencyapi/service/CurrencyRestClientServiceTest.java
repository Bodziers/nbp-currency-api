package pl.finmatik.nbpcurrencyapi.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.reactive.server.WebTestClient;

import pl.finmatik.nbpcurrencyapi.domain.Rate;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CurrencyRestClientServiceTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CurrencyRestClientService clientService;

    @Test
    void testApiRequestForCurrencyTable() {
        webTestClient.get()
                .uri("http://api.nbp.pl/api/exchangerates/tables/a/{date}?format=json", "2022-03-01")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertNotNull(response.getResponseBody()));
    }

    @Test
    void testServcieToGetCurrencyTable() {
        String date = "2022-03-01";
        List<Rate> rates = clientService.retrieveCurrencyTable(date);
        System.out.println(rates);
        Assertions.assertNotNull(rates);
        Assertions.assertTrue(rates.size()>0);
    }

    @Test
    void retrieveNullCurrencyTable() throws Exception {
        String date = "1022-01-01";
        //then
        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            clientService.retrieveCurrencyTable(date);
        });
        Assertions.assertEquals("pl.finmatik.nbpcurrencyapi.exception.NbpRatesNotFoundException: 404 url not found exception", thrown.getMessage());
    }
}
