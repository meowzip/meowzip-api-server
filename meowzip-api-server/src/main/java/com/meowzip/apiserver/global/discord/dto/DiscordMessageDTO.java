package com.meowzip.apiserver.global.discord.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscordMessageDTO {

    private String content;
    private List<Embed> embeds;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embed {
        private String title;
        private String description;
    }
}
