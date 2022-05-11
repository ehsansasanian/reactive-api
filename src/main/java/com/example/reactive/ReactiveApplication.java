package com.example.reactive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@SpringBootApplication
public class ReactiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveApplication.class, args);
    }

}

@RestController
@RequiredArgsConstructor
class ReactiveController {
    private final IntervalProducer intervalProducer;

    @GetMapping(produces = TEXT_EVENT_STREAM_VALUE, value = "/app/{name}")
    public Publisher<DemoResponse> stringPublisher(@PathVariable String name) {
        return this.intervalProducer.produce(name);
    }

}

@Component
class IntervalProducer {
    public Flux<DemoResponse> produce(String name) {
        return Flux.fromStream(Stream.generate(() -> "Hi " + name + ",   This message produced at " + Instant.now()))
                .map(DemoResponse::new)
                .delayElements(Duration.ofSeconds(1));
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class DemoResponse {
    private String message;
}