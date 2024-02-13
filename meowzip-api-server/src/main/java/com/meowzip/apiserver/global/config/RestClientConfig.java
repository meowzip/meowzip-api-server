package com.meowzip.apiserver.global.config;

import com.meowzip.apiserver.global.discord.component.DiscordComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Component
public class RestClientConfig {

    @Value("${discord.webhook-url}")
    private String webhookUrl;

    @Bean
    public DiscordComponent discordComponent() {
        RestClient restClient = RestClient.builder()
                .baseUrl(webhookUrl)
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(DiscordComponent.class);
    }
}
