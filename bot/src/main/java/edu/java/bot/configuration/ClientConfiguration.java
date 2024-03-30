package edu.java.bot.configuration;

import edu.java.bot.clients.ScrapperLinkWebClient;
import edu.java.bot.clients.ScrapperTgChatWebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

@Configuration
public class ClientConfiguration {
    @Bean
    public ScrapperLinkWebClient scrapperLinkWebClient(
        ApplicationConfig applicationConfig,
        ExchangeFilterFunction scrapperRetryFilter
    ) {
        return ScrapperLinkWebClient.create(applicationConfig.scrapperClient().url(), scrapperRetryFilter);
    }

    @Bean
    public ScrapperTgChatWebClient scrapperTgChatWebClient(
        ApplicationConfig applicationConfig,
        ExchangeFilterFunction scrapperRetryFilter
    ) {
        return ScrapperTgChatWebClient.create(applicationConfig.scrapperClient().url(), scrapperRetryFilter);
    }
}
