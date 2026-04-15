package com.hrbu.aidemo.dto;

import lombok.Data;

@Data
public class ChatFormDTO {
    //用户id
    private Long id;
    //请求信息
    private String message;
}
