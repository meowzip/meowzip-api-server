package com.meowzip.apiserver.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonListResponse<T> extends CommonResponse<T> {

    @Builder.Default
    private int total = 0;

    // TODO: 추후 재검토
//    @Builder.Default
//    private boolean hasNext = false;

    @Builder.Default
    private List<T> items = new ArrayList<>();

    public CommonListResponse(HttpStatus status) {
        super(status);
        this.items = new ArrayList<>();
    }

    public CommonListResponse<T> add(List<T> data) {
        this.total += data.size();
        this.items.addAll(data);

        return this;
    }
}
