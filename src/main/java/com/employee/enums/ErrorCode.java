package com.employee.enums;

import lombok.Getter;
import lombok.Setter;

public enum ErrorCode {

    NAME_ALREADY_EXISTS(
            "409-001",
            "This name already exists.",
            409),

    ;

    @Getter
    private Data data;

    ErrorCode(String code, String description, int httpResponseCode) {
        this.data = new Data(code, description, httpResponseCode);
    }


    public final class Data {

        @Getter
        private String code;

        @Getter
        private String description;

        @Getter
        private int httpResponseCode;

        @Setter
        @Getter
        private String label;

        Data(String code, String description, int httpResponseCode) {
            this.code = code;
            this.description = description;
            this.httpResponseCode = httpResponseCode;
        }
    }
}
