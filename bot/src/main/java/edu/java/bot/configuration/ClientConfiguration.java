package edu.java.bot.configuration;

import edu.java.bot.clients.ScrapperLinkWebClient;
import edu.java.bot.clients.ScrapperTgChatWebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Bean
    public ScrapperLinkWebClient scrapperLinkWebClient(ApplicationConfig applicationConfig) {
        return ScrapperLinkWebClient.create(applicationConfig.clientBaseUrl().scrapperUrl());
    }

    @Bean
    public ScrapperTgChatWebClient scrapperTgChatWebClient(ApplicationConfig applicationConfig) {
        return ScrapperTgChatWebClient.create(applicationConfig.clientBaseUrl().scrapperUrl());
    }
}
