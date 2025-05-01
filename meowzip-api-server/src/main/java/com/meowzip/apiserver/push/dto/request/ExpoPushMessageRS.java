package com.meowzip.apiserver.push.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpoPushMessageRS {

    private ResponseData data;

    public String toString() {
        return "data: {" + data.toString();
    }

    @Data
    @AllArgsConstructor
    public static class ResponseData {

        private String id;
        private String status;

        public String toString() {
            return "{id: " + id + ", status: " + status + "}";
        }
    }
}
