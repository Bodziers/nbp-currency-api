package pl.finmatik.nbpcurrencyapi.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.finmatik.nbpcurrencyapi.domain.CurrencyTable;
import pl.finmatik.nbpcurrencyapi.domain.Rate;
import pl.finmatik.nbpcurrencyapi.exception.NbpRatesNotFoundException;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CurrencyRestClientService {

    private String baseUrl = "http://api.nbp.pl/api/exchangerates/tables/a/";

    private final WebClient webClient;

    public CurrencyRestClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    /*
        Working with WebClient which will replace restTemplate and uses reactive stack.
        To work with WebClient we need to create a WebClient object, here with builder and default settings.
        I am using Mono because the results are returned at once. To be able to deserialize List of Rate
        which is generic we need to use ParameterizedTypeReference.
     */
    //TODO handle null in return (throw exception or error)
    public List<Rate> retrieveCurrencyTable(String date) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("{date}")
                        .queryParam("format", "json")
                        .build(date))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, errorResponse ->
                        Mono.error(new NbpRatesNotFoundException("404 url not found exception"))
                )
                .onStatus(HttpStatus::is5xxServerError, errorResponse ->
                        Mono.error(new NbpRatesNotFoundException("505 server is not responding"))
                )
                .bodyToMono(new ParameterizedTypeReference<List<CurrencyTable>>() {})
                .map(response -> response.get(0).getRates())
                .block();
    }
}
