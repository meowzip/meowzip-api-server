package com.meowzip.apiserver.member.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.meowzip.member.entity.LoginType;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record EmailExistsResponseDTO(
        boolean isEmailExists,
        LoginType loginType
) {}
