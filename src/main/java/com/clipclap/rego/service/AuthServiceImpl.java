package com.clipclap.rego.service;

import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.repository.UserRepository;
import com.clipclap.rego.validation.JoinForm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


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
        apiInfo.setRole("ROLE_USER");		        // 기본 역할 'ROLE_USER'
        apiInfo.setNickname(joinForm.getNickname());
        apiInfo.setGender(joinForm.getGender());
        apiInfo.setBirthDate(joinForm.getBirthDate());
        apiInfo.setWithdrawalRequest(false);        // 탈퇴요청 Default - false

        userRepository.save(apiInfo);

        return "";
    }

    // 이메일 매개변수를 사용하는 메서드의 이름을 변경합니다.
    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user;
        } else {
            throw new DataNotFoundException("User NOT FOUND");
        }
    }

    // 유저네임 매개변수를 사용하는 메서드는 그대로 둡니다.
    @Override
    public User getUser(String username) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(username));
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new DataNotFoundException("User NOT FOUND");
        }
    }


    @Override
    public String nicknameDuplicateCheck(String nickname) {
        Optional<User> optionalUser = userRepository.findByNickname(nickname);

        if(optionalUser.isPresent()){
            return "Duplicate";
        } else {
            return "No_Duplicate";
        }
    }

    @Override
    public Page<User> getMemberList(Pageable pageable){
        Page<User> memberList=userRepository.findAll(pageable);
        return memberList;
    }
}
