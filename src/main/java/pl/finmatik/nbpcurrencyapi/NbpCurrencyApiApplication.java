package pl.finmatik.nbpcurrencyapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class NbpCurrencyApiApplication {


    public static void main(String[] args) {
        SpringApplication.run(NbpCurrencyApiApplication.class, args);
    }

}
