package com.clipclap.rego.controller;


import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.service.UserService;
import com.clipclap.rego.validation.JoinForm2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordUpdateController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/updatePw")
    public String showUpdatePasswordForm(Model model, @RequestParam String email) {
        // 업데이트 폼을 보여줄 때 필요한 데이터를 모델에 추가
        model.addAttribute("email", email);
        model.addAttribute("joinForm2", new JoinForm2());

        // User 객체를 모델에 추가
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);

        return "updatePw"; // 업데이트 폼을 보여주는 HTML 페이지
    }

    @PostMapping("/updatePw")
    public String updatePassword(@ModelAttribute("joinForm2") JoinForm2 joinForm2,
                                 @RequestParam(name = "email") String email,
                                 Model model) {

        // 주어진 이메일 파라미터를 기반으로 사용자를 찾음
        User currentUser = userService.getUserByEmail(email);
        if (currentUser == null) {
            model.addAttribute("updateResult", "해당 이메일의 사용자를 찾을 수 없습니다.");
            model.addAttribute("email", email);  // 실패한 경우 이메일 값을 다시 모델에 추가
            return "updatePw";
        }

        // 새 비밀번호와 새 비밀번호 확인이 일치하는지 확인
        String newPassword1 = joinForm2.getNewPassword1();
        String newPassword2 = joinForm2.getNewPassword2();

        if (newPassword1.equals(newPassword2)) {
            // 새 비밀번호와 새 비밀번호 확인이 일치하는 경우
            String hashedPassword = passwordEncoder.encode(newPassword1);
            currentUser.setPassword(hashedPassword);

            // DB에 변경된 사용자 정보 저장
            userService.saveOrUpdate(currentUser);

            model.addAttribute("updateResult", "비밀번호가 업데이트되었습니다.");
            return "updateComplete";
        } else {
            // 새 비밀번호와 새 비밀번호 확인이 일치하지 않는 경우
            model.addAttribute("updateResult", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("email", email);  // 실패한 경우 이메일 값을 다시 모델에 추가
        }

        return "updatePw";
    }





















}




