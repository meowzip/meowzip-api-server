package com.meowzip.apiserver.global.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CommonListResponse<T> {

    private HttpStatus status;

    @Builder.Default
    private int total = 0;

    // TODO: 추후 재검토
//    @Builder.Default
//    private boolean hasNext = false;

    @Builder.Default
    private List<T> items = new ArrayList<>();

    public CommonListResponse(HttpStatus status) {
        this.status = status;
        this.items = new ArrayList<>();
    }

    public CommonListResponse<T> add(List<T> data) {
        this.total += data.size();
        this.items.addAll(data);

        return this;
    }
}
