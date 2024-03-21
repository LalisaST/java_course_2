package edu.java.configuration;

import edu.java.client.BotWebClient;
import edu.java.client.GitHubWebClient;
import edu.java.client.StackOverflowWebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Bean
    public GitHubWebClient gitHubClient(ApplicationConfig applicationConfig) {
        return GitHubWebClient.create(applicationConfig.clientBaseUrl().githubUrl());
    }

    @Bean
    public StackOverflowWebClient stackOverflowClient(ApplicationConfig applicationConfig) {
        return StackOverflowWebClient.create(applicationConfig.clientBaseUrl().stackoverflowUrl());
    }

    @Bean
    public BotWebClient botWebClient(ApplicationConfig applicationConfig) {
        return BotWebClient.create(applicationConfig.clientBaseUrl().botUrl());
    }
}
