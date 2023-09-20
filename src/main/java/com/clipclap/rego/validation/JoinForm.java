package com.clipclap.rego.validation;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class JoinForm {

    private String email;

//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
//            message = "비밀번호는 대문자, 소문자, 숫자를 모두 포함해야 합니다.")
    @NotEmpty
    private String password;

    @NotEmpty(message = "성별은 필수 입력 항목입니다.")
    private String gender;

    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birthDate;

    @NotEmpty
    @Size(min = 4, max = 12, message = "닉네임은 4자에서 12자 사이여야 합니다.")
    private String nickname;

    private String role;
}
