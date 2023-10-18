package com.clipclap.rego.controller;

import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.service.DataNotFoundException;
import com.clipclap.rego.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class MyPageController {

    private final UserService userService;

    @GetMapping("/myPage")
    public String getUserNickname(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            try {
                User userInfo = userService.getUserByEmail(email);
                model.addAttribute("userInfo", userInfo);
            } catch (DataNotFoundException e) {
                // 이메일로 사용자를 찾을 수 없을 때의 처리
                model.addAttribute("errorMessage", "사용자 정보를 찾을 수 없습니다.");
                return "main";
            }
        }
        return "myPage";
    }
}


