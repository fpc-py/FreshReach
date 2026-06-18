package com.takeout.xianda.vo;

import lombok.Data;

@Data
public class CaptchaVO {
    private String codeKey;
    private  String imgBase64;
}
