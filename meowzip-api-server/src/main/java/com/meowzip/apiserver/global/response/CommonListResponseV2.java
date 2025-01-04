package com.meowzip.apiserver.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CommonListResponseV2<T> {

    private HttpStatus status;

    private int total = 0;

    private boolean hasNext = false;

    private List<T> items = new ArrayList<>();

    public CommonListResponseV2(HttpStatus status) {
        this.status = status;
        this.items = new ArrayList<>();
    }

    public CommonListResponseV2<T> add(List<T> data, boolean hasNext) {
        this.total += data.size();
        this.items.addAll(data);
        this.hasNext = hasNext;

        return this;
    }
}
