package com.meowzip.apiserver.global.discord.component;

import com.meowzip.apiserver.global.discord.dto.DiscordMessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
@Component
public interface DiscordComponent {

    @PostExchange
    ResponseEntity<String> sendMessage(@RequestBody DiscordMessageDTO message);
}
