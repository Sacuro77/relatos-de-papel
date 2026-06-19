package com.actividad2.orders_service.client;

import com.actividad2.orders_service.dto.BookAvailabilityResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CatalogueClient {

    private final WebClient.Builder webClientBuilder;

    public CatalogueClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public BookAvailabilityResponse checkAvailability(Long bookId, Integer quantity) {
        return webClientBuilder.build()
                .get()
                .uri(
                        "http://catalogue-service/api/v1/books/{bookId}/availability?quantity={quantity}",
                        bookId,
                        quantity
                )
                .retrieve()
                .bodyToMono(BookAvailabilityResponse.class)
                .block();
    }
}