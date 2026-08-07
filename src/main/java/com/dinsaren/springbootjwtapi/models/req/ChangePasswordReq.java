package com.dinsaren.springbootjwtapi.models.req;

import lombok.Data;

@Data
public class ChangePasswordReq {

    private String oldPassword;

    private String newPassword;

    private String confirmPassword;

}