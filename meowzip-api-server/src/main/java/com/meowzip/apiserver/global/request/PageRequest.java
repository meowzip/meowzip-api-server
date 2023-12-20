package com.meowzip.apiserver.global.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest {

    private int page = 0;
    private int size = 9;

    public int getPage() {
        return this.page <= 0 ? 0 : this.page - 1;
    }

    public int getSize() {
        return this.size == 0 ? 9 : this.size;
    }

    public int getOffset() {
        return getPage() * getSize();
    }

    public org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(getPage(), getSize());
    }
}
