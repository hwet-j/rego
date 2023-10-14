package com.clipclap.rego.controller;

import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class FindPwController {
    @Autowired
    private UserRepository userRepository;



    @PostMapping("/findPw")
    public ResponseEntity<?> processPasswordResetForm(@RequestParam String email, Model model) {
        System.out.println("memberOptional");
        Optional<User> memberOptional = userRepository.findByEmail(email);
        if (memberOptional.isPresent()) {
            // 사용자를 찾았을 때, 사용자 정보를 모델에 추가하여 updatePw 폼으로 이동
            User user = memberOptional.get();
            model.addAttribute("user", user);
            System.out.println("사용자를 찾았습니다: " + user.getEmail());
            return ResponseEntity.ok("사용자를 찾았습니다: " + user.getEmail()); // 사용자를 찾은 경우 updatePw 폼으로 직접 이동
        } else {
            model.addAttribute("error", "일치하는 사용자를 찾을 수 없습니다.");
            System.out.println("사용자를 찾지 못했습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("일치하는 사용자를 찾을 수 없습니다.");
        }

    }


}