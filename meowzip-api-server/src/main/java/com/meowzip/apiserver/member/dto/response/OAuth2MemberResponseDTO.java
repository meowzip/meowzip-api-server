package com.meowzip.apiserver.member.dto.response;

import com.meowzip.member.entity.LoginType;

public record OAuth2MemberResponseDTO(
        LoginType loginType
) {
}
