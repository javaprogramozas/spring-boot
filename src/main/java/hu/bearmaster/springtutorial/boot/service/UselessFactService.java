package hu.bearmaster.springtutorial.boot.service;

import hu.bearmaster.springtutorial.boot.model.response.FactResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Locale;

@Service
public class UselessFactService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UselessFactService.class);

    private final WebClient webClient;

    public UselessFactService(WebClient.Builder builder, @Value("${facts.url}") String factsUrl) {
        this.webClient = builder
                .baseUrl(factsUrl)
                .build();
    }

    public FactResponse getFact(String type, Locale locale) {
        ResponseEntity<FactResponse> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v2/facts/{type}")
                        .queryParam("language", locale.getLanguage())
                        .build(type))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(FactResponse.class)
                .block();

        LOGGER.info("Fact response: {}", response);
        return response.getBody();
    }

}
