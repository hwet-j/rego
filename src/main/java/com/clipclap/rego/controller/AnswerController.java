package com.clipclap.rego.controller;


import com.clipclap.rego.model.entitiy.Answer;
import com.clipclap.rego.model.entitiy.Question;
import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.service.AnswerService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService; // UserService 주입

    // 답변 추천
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(@PathVariable("id") Integer id, Principal principal, Model model) {
        Answer answer = answerService.getAnswer(id);
        User user = userService.getUser(principal.getName()); // userService를 사용하여 User 객체 가져옴
        answerService.vote(answer, user);
        return String.format("redirect:/question/detail/%d", answer.getQuestion().getId());
    }

    // 답변 삭제
    @GetMapping("/delete/{id}")
    public String answerDelete(@PathVariable("id") Integer id, Principal principal) {
        Answer answer = answerService.getAnswer(id);
        if (!answer.getWriter().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        answerService.delete(answer);
        return "redirect:/question/detail/" + answer.getQuestion().getId();
    }


    // 답변 수정 처리
    @PostMapping("/modify/{id}")
    public String modify(@Valid AnswerForm answerForm, BindingResult bindingResult, @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "answer_form";
        }
        Answer answer = answerService.getAnswer(id);
        if (!answer.getWriter().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        answerService.modify(answer, answerForm.getContent());
        return String.format("redirect:/question/detail/%d", answer.getQuestion().getId());
    }

    // 답변 등록 처리
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add/{id}")
    public String addAnswer(@PathVariable("id") Integer id, Model model, @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {
        Question question = questionService.getQuestion(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }
        User user = userService.getUser(principal.getName());
        answerService.add(question, answerForm.getContent(), user);
        return String.format("redirect:/question/detail/%d", id);
    }
}
