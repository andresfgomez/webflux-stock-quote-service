package guru.springframework.webfluxstockquoteservice.web;

import guru.springframework.webfluxstockquoteservice.model.Quote;
import guru.springframework.webfluxstockquoteservice.service.QuoteGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class QuoteHandler {

    private final QuoteGeneratorService quoteGeneratorService;

    public Mono<ServerResponse> fetchQuotes(ServerRequest request) {
        int size = request.queryParam("size").map(Integer::parseInt).orElse(10);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(quoteGeneratorService.fetchQuoteStream(Duration.ofMillis(100)).take(size), Quote.class);
    }

    public Mono<ServerResponse> streamQuotes(ServerRequest request) {
        int size = request.attribute("size").map(param -> Integer.parseInt((String) param)).orElse(10);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(quoteGeneratorService.fetchQuoteStream(Duration.ofMillis(200)), Quote.class);
    }

}
