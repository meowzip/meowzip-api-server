package com.meowzip.apiserver.member.dto.request;

public record LoginRequestDTO(
        String email,
        String password
) { }
