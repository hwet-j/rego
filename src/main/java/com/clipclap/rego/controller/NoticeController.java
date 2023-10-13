package com.clipclap.rego.controller;


import com.clipclap.rego.model.entitiy.Notice;
import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.service.NoticeService;
import com.clipclap.rego.service.UserService;
import com.clipclap.rego.validation.NoticeForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/notice")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@Controller
public class NoticeController {

    private final UserService userService;
    private final NoticeService noticeService;
        // 삭제 처리
        @GetMapping("/delete/{id}")
        public String noticeDelete(@PathVariable("id") Integer id, Principal principal) {

            Notice notice = noticeService.getNotice(id);
            if (!notice.getWriter().getEmail().equals(principal.getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
            }
            noticeService.delete(notice);
            return "redirect:/notice/list";
        }

        // 공지 수정 폼을 보여줌
        @PreAuthorize("isAuthenticated()") //인증을 요하는 메서드
        @GetMapping("/modify/{id}")
        public String noticeModify(NoticeForm noticeForm,
                                   @PathVariable("id") Integer id, Principal principal){
            //1.파라미터받기
            //2.비즈니스로직수행
            Notice notice = noticeService.getNotice(id); //질문상세
            if ( !notice.getWriter().getEmail().equals(principal.getName()) ) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
            }

            noticeForm.setSubject(notice.getSubject());
            noticeForm.setContent(notice.getContent());
            //3.Model  //4.View
            return "notice_form"; //질문등록폼으로 이동
        }


        // 공지 등록 폼을 보여줌
        @PreAuthorize("isAuthenticated()")
        @GetMapping("/add")
        public String add(NoticeForm noticeForm, Model model) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetail = (UserDetails) auth.getPrincipal();

            String username = userDetail.getUsername();
            NoticeForm noticeForm1 = new NoticeForm();


            model.addAttribute("noticeForm", noticeForm); // Use the provided noticeForm
            return "notice_form";
        }

    // 공지 등록 처리
    @PostMapping("/add")
    public String noticeAdd(@Valid NoticeForm noticeForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            System.out.println("fail");
            return "notice_form";
        }

        User user = userService.getUserByEmail(principal.getName());

        // NoticeService.add 메서드에서 공지 생성 후 ID를 반환하도록 수정해야 합니다.
        Integer Id = noticeService.add(noticeForm, user);

        // 공지 상세 페이지로 리디렉션합니다.
        return "redirect:/notice/detail/" + Id;
    }

        // 공지 상세 조회
        @GetMapping("/detail/{id}")
        public String detail(@PathVariable("id") Integer id, Model model) {
            Notice notice = noticeService.getNotice(id);
            model.addAttribute("notice", notice);
            return "notice_detail";
        }

        // 페이징 기능이 있는 공지 목록 조회
        @GetMapping("/list")
        @PreAuthorize("isAuthenticated()")
        public String noticeList(Model model,
                                   @RequestParam(value = "page", defaultValue = "0") int page) {
            Page<Notice> noticePage = noticeService.getList(page);
            model.addAttribute("noticePage", noticePage);
            return "notice_list";
        }

}
