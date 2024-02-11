package com.meowzip.apiserver.global.discord.feign;

import com.meowzip.apiserver.global.discord.dto.DiscordMessageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "discordClient", url = "${discord.webhook-url}")
public interface DiscordClient {

    @PostMapping
    ResponseEntity<String> sendMessage(@RequestBody DiscordMessageDTO message);
}
