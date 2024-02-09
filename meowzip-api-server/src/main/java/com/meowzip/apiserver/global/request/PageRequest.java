package com.meowzip.apiserver.global.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

@Schema
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest {

    @Schema(description = "페이지 번호", example = "0")
    private int page = 0;

    @Schema(description = "페이지 크기", example = "9")
    private int size = 9;

    public int getPage() {
        return this.page <= 0 ? 0 : this.page - 1;
    }

    public int getSize() {
        return this.size == 0 ? 9 : this.size;
    }

    @JsonIgnore
    public int getOffset() {
        return getPage() * getSize();
    }

    public org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(getPage(), getSize());
    }
}
