package com.clipclap.rego.controller;


import com.clipclap.rego.model.entitiy.Answer;
import com.clipclap.rego.model.entitiy.Question;
import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.service.AnswerService;
import com.clipclap.rego.service.EmailService;
import com.clipclap.rego.service.QuestionService;
import com.clipclap.rego.service.UserService;
import com.clipclap.rego.validation.AnswerForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Map;

@RequestMapping("/answer")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@Controller
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService; // UserService 주입
    private final EmailService emailService;


    // 답변 삭제
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String answerDelete(@PathVariable("id") Integer id, Principal principal) {
        Answer answer = answerService.getAnswer(id);

        answerService.delete(answer);
        return "redirect:/question/detail/" + answer.getQuestion().getId();
    }


    // 답변 수정 처리
    @RequestMapping("/modify/{id}")
    public String modify(@RequestBody Map<String, String> requestBody, @PathVariable("id") Integer id, Principal principal) {

        String editedContent = requestBody.get("content");


        Answer answer = answerService.getAnswer(id);
        if (!answer.getWriter().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        answerService.modify(answer, editedContent);
        return String.format("redirect:/question/detail/%d", answer.getQuestion().getId());
    }

    // 답변 등록 처리

    @PostMapping("/add/{id}")
    public String addAnswer(@PathVariable("id") Integer id, Model model, @Valid AnswerForm answerForm,
                            BindingResult bindingResult, Principal principal, String email) {

        // 이메일 보내기
        String toEmail = email;
        String subject = "새로운 답변이 등록되었습니다.";
        String message = "새로운 답변 내용: " + answerForm.getContent();

        emailService.sendEmail(toEmail, subject, message);

        Question question = questionService.getQuestion(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }
        User user = userService.getUserByEmail(principal.getName());
        answerService.add(question, answerForm.getContent(), user);
        return String.format("redirect:/question/detail/%d", id);
    }

}
