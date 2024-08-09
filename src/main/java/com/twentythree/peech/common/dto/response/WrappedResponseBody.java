package com.twentythree.peech.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WrappedResponseBody<T> {

    private int statusCode;
    private T responseBody;

}
