package edu.java.bot.retry;

import edu.java.bot.configuration.ApplicationConfig;
import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

public class LinearRetryFilter implements ExchangeFilterFunction {
    private final int maxAttempts;
    private final Duration initDelay;
    private final Set<Integer> codes;

    public LinearRetryFilter(ApplicationConfig.Client client) {
        ApplicationConfig.Client.Retry retry = client.retry();
        this.maxAttempts = retry.maxAttempts();
        this.initDelay = retry.initDelay();
        this.codes = Objects.requireNonNullElse(retry.codes(), Set.of());
    }

    @Override
    public @NotNull Mono<ClientResponse> filter(@NotNull ClientRequest request, @NotNull ExchangeFunction next) {
        return doRequest(request, next, 1);
    }

    private Mono<ClientResponse> doRequest(ClientRequest request, ExchangeFunction next, int retryCount) {
        return next.exchange(request)
            .flatMap(response -> {
                if (codes.contains(response.statusCode().value())
                    && retryCount <= maxAttempts) {
                    Duration delay = initDelay.multipliedBy(retryCount);
                    return Mono.delay(delay)
                        .then(Mono.defer(() -> doRequest(request, next, retryCount + 1)));
                } else {
                    return Mono.just(response);
                }
            });
    }
}
