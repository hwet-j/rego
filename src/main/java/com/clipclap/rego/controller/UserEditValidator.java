package com.clipclap.rego.controller;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEditValidator {


    private Integer id;

    private String memberImage;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$|^$",
            message = "비밀번호는 최소 8자 길이, 대문자, 소문자, 숫자, 특수 문자가 포함되어야 합니다.")
    private String password;

    private String userprofile;

}