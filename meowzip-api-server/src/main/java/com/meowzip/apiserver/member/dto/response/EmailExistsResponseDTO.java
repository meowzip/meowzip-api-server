package com.meowzip.apiserver.member.dto.response;

import lombok.Getter;

public record EmailExistsResponseDTO(
        boolean isEmailExists
) {}
