package com.clipclap.rego.controller;


import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
    private final AuthService authService;

    @GetMapping("/user/list")
    public String memberList(Model model, @PageableDefault(size = 10) Pageable pageable) throws Exception {
        Page<User> userList = authService.getMemberList(pageable);
        int nowPage=userList.getPageable().getPageNumber()+1;
        int startPage=Math.max(nowPage-3,1);
        int endPage=Math.min(nowPage+3,userList.getTotalPages());
        int firstPage=Math.max(0,0);
        int lastPage=userList.getTotalPages()-1;

        model.addAttribute("userList",userList);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("firstPage",firstPage);
        model.addAttribute("lastPage",lastPage);

        return "admin_member";
    }
}
