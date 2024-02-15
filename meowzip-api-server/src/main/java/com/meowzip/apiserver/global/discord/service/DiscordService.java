package com.meowzip.apiserver.global.discord.service;

import com.meowzip.apiserver.global.discord.component.DiscordComponent;
import com.meowzip.apiserver.global.discord.dto.DiscordMessageDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiscordService {
    
    private final DiscordComponent discordComponent;

    public void send(HttpServletRequest req, HttpStatus status, String content) {
        if (req.getRequestURI().equals("/health-check")) {
            return;
        }

        String errorMessage = """
                - IP: {{IP}}
                - API: {{URI}}
                """
                .replace("{{IP}}", req.getHeader("X-FORWARDED-FOR") == null ? req.getRemoteAddr() : req.getHeader("X-FORWARDED-FOR"))
                .replace("{{URI}}", req.getRequestURL());

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        DiscordMessageDTO message = DiscordMessageDTO.builder()
                .content(errorMessage)
                .embeds(List.of(DiscordMessageDTO.Embed.builder()
                        .title(date)
                        .description(String.format("%s: %s", status, content))
                        .build()))
                .build();

        ResponseEntity<String> response = discordComponent.sendMessage(message);

        if (response.getStatusCode().value() != HttpStatus.NO_CONTENT.value()) {
            log.error("Failed to send message to discord. status: {}", response.getStatusCode());
        }
    }
}
