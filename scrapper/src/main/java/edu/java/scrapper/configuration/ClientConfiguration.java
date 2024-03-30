package edu.java.scrapper.configuration;

import edu.java.scrapper.client.BotWebClient;
import edu.java.scrapper.client.GitHubWebClient;
import edu.java.scrapper.client.StackOverflowWebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

@Configuration
public class ClientConfiguration {
    @Bean
    public GitHubWebClient gitHubClient(ApplicationConfig applicationConfig, ExchangeFilterFunction gitHubRetryFilter) {
        return GitHubWebClient.create(applicationConfig.botClient().url(), gitHubRetryFilter);
    }

    @Bean
    public StackOverflowWebClient stackOverflowClient(
        ApplicationConfig applicationConfig,
        ExchangeFilterFunction stackOverflowRetryFilter
    ) {
        return StackOverflowWebClient.create(applicationConfig.stackoverflowClient().url(), stackOverflowRetryFilter);
    }

    @Bean
    public BotWebClient botWebClient(ApplicationConfig applicationConfig, ExchangeFilterFunction botRetryFilter) {
        return BotWebClient.create(applicationConfig.githubClient().url(), botRetryFilter);
    }
}
