package com.clipclap.rego.service;


import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    /*우리는  SecurityConfig.java에서 PasswordEncoder을 Bean등록해두었다
참고 BCryptPasswordEncoder클래스는 스프링 시큐리티에서 제공되는 클래스이다.
    이 클래스이용해서  패스워드를 암호화해서 처리하도록 한다.
    bcrypt는 패스워크드를 저장하는 용도로 설계된 해시 함수로
    특정 문자열을 암호화하고,
    체크하는 쪽에서는 암호화된 패스워드가 가능한 패스워드인지만 확인하고
    다시 원문으로 되돌리지는 못한다.(교재 p651참고)*/

    //회원가입처리
    public User create(String username, String email, String password){
        //여기에서는 일단 (컨트롤러로부터는 DTO로 받아서 작업할 예정)
        //StieUser Enity로 UserRepository완 연동
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        //암호는 스프링시큐리티를 이용해서 암호화하여 비번을 저장
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return user;
    }

    // 이메일 매개변수를 사용하는 메서드의 이름을 변경합니다.
    public User getUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new DataNotFoundException("User NOT FOUND");
        }
    }

    public void saveOrUpdate(User user) {
        userRepository.save(user);
    }

    // 유저네임 매개변수를 사용하는 메서드는 그대로 둡니다.
    public User getUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new DataNotFoundException("User NOT FOUND");
        }
    }



}





