package com.clipclap.rego.validation;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class JoinForm2 {

    private String email;

    //    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
//            message = "비밀번호는 대문자, 소문자, 숫자를 모두 포함해야 합니다.")
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotEmpty(message = "")
    private String passwordConfirm;

    @NotEmpty(message = "비밀번호는 필수입력입니다.")
    private String newPassword1;   //비밀번호

    @NotEmpty(message = "비밀번호 확인은 필수입력입니다.")
    private String newPassword2;

    @NotEmpty(message = "성별을 선택해주세요.")
    private String gender;

    @Past(message = "생년월일을 다시 확인해주세요.")
    private LocalDate birthDate;

    @Size(min = 4, max = 12, message = "닉네임은 4자에서 12자 사이여야 합니다.")
    private String nickname;

    private String role;
}
