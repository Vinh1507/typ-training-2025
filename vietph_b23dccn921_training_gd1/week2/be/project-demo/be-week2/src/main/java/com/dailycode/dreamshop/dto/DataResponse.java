package com.dailycode.dreamshop.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataResponse {
    private HttpStatus status;
    private boolean success;
    private String message;
    private Object data;
}
