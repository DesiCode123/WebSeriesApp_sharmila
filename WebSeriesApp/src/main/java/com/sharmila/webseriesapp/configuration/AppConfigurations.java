package com.sharmila.webseriesapp.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfigurations {

    @Value("${movies.api.url}")
    public String baseUrl;

    // gooogle seach me the configuration for webclient of spring webflux with data limit more than 1 mb
    @Bean
    public WebClient webClient() {
        return WebClient
                .builder()
                .baseUrl(baseUrl)
                .exchangeStrategies(ExchangeStrategies
                        .builder()
                        .codecs(codecs -> codecs
                                .defaultCodecs()
                                .maxInMemorySize(1024 * 1024))
                        .build())
                .build();
    }
}
