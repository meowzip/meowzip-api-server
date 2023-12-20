package com.meowzip.apiserver.cat.dto.response;

import com.meowzip.cat.entity.Cat;

public record CatResponseDTO(
        Long id,
        String imageUrl,
        String name,
        boolean isCoParented
) {

    public CatResponseDTO(Cat cat) {
        this(cat.getId(), cat.getImageUrl(), cat.getName(), cat.isCoParented());
    }
}
