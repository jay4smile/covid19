package com.jp.covid19.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDetails {
    private String message;
    private Object responseObject;
    private UserDTO user;
    private String token;
}
