package com.wz.modules.sys.vo;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;
    private String captcha;
    private boolean isRememberMe;
}
