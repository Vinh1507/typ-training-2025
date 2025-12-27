package com.vn.Project_Trainning_Week2.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ResponseDTO<T> {
    private int code;
    private String message;
    private T data;
}
