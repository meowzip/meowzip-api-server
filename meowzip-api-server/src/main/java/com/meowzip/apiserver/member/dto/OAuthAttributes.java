package com.meowzip.apiserver.member.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum OAuthAttributes {

    GOOGLE("google", (attributes) -> new UserProfile(
            UUID.randomUUID().toString(),
            (String) attributes.get("email")
    )),
    ;

    private final String registrationId;
    private final Function<Map<String, Object>, UserProfile> of;

    public static UserProfile extract(String loginProvider, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> provider.registrationId.equals(loginProvider))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}
