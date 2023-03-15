package com.huliua.miniprogram.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BusinessException extends Exception{
    private Integer code;
    private String msg;
}
