package com.huliua.miniprogram.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class BusinessException extends Exception{
    private Integer code;
    private String message;
}
