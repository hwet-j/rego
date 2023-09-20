package com.clipclap.rego.service;

import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.repository.UserRepository;
import com.clipclap.rego.validation.JoinForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String join(JoinForm joinForm) {
        User apiInfo = userRepository.findByEmail(joinForm.getEmail());
        String rawPassword = joinForm.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        apiInfo.setPassword(encPassword);
        apiInfo.setRole("ROLE_USER");		// 기본 역할 'ROLE_USER'
        apiInfo.setNickname(joinForm.getNickname());
        apiInfo.setGender(joinForm.getGender());
        apiInfo.setBirthDate(joinForm.getBirthDate());
        System.out.println("회원가입 진행 : " + apiInfo);
        userRepository.save(apiInfo);

        return "";
    }
}
