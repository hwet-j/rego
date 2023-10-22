package com.clipclap.rego.controller;

import com.clipclap.rego.model.dto.PlanCard;
import com.clipclap.rego.model.dto.PlannerDTO;
import com.clipclap.rego.model.entitiy.LikeAttraction;
import com.clipclap.rego.model.entitiy.Planner;
import com.clipclap.rego.model.entitiy.Question;
import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.repository.PlannerRepository;
import com.clipclap.rego.repository.QuestionRepository;
import com.clipclap.rego.service.*;
import com.clipclap.rego.validation.JoinForm2;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class MyPageController {
    @Autowired
    private UserService userService;

    @Autowired
    private ImageUpload imageUpload;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private final QuestionRepository questionRepository;

    @Autowired
    private final PlannerService plannerService;

    @Autowired
    private final LikeService likeService;

    @GetMapping("/myPage")
    public String getUserNickname(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            try {
                User userInfo = userService.getUserByEmail(email);
                List<Question> userQuestions = questionRepository.findByWriter_Email(email);
                List<PlannerDTO> myPlanners = plannerService.findByUserEmail(email);
                List<Integer> userPlanVotes = plannerService.userPlanVotes(email);
                List<PlanCard> myVotePlanList =  plannerService.userPlanVotePlans(userPlanVotes);
                List<LikeAttraction> userAttractionLikes = likeService.getUserLikes(email);


                model.addAttribute("userInfo", userInfo);
                model.addAttribute("userQuestions", userQuestions);
                model.addAttribute("myPlanners", myPlanners);
                model.addAttribute("myVotePlanList", myVotePlanList);

                model.addAttribute("userAttractionLikes", userAttractionLikes);


                System.out.println(myPlanners);
            } catch (DataNotFoundException e) {
                // 이메일로 사용자를 찾을 수 없을 때의 처리
                model.addAttribute("errorMessage", "사용자 정보를 찾을 수 없습니다.");
                return "main";
            }
        }
        return "myPage";
    }






    @GetMapping("myPageModifyConfirm")
    public String yourMethod(Model model) {
        model.addAttribute("joinForm2", new JoinForm2());
        return "myPageModifyConfirm";
    }

    @PostMapping("/myPageModifyConfirm")
    public String processModifyConfirmPage(JoinForm2 joinForm2, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            model.addAttribute("errorMessage", "인증되지 않은 사용자입니다.");
            return "main";
        }

        if (!authentication.isAuthenticated()) {
            model.addAttribute("errorMessage", "인증되지 않은 사용자입니다.");
            return "main";
        }

        String email = authentication.getName();
        try {
            User userInfo = userService.findByEmail(email);
            if (userService.isPasswordCorrect(userInfo, joinForm2.getPassword())) {
                return "redirect:/myPageModify";
            } else {
                model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
                return "myPageModifyConfirm";
            }
        } catch (DataNotFoundException e) {
            model.addAttribute("errorMessage", "사용자 정보를 찾을 수 없습니다.");
            return "main";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "오류가 발생했습니다.");
            return "main";
        }
    }




    @GetMapping("/myPageModify")
    public String getUserInfo(Model model, JoinForm2 joinForm2) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            try {
                User userInfo = userService.findByEmail(email); // 이 부분을 수정합니다.
                model.addAttribute("userInfo", userInfo);
            } catch (DataNotFoundException e) {
                // 이메일로 사용자를 찾을 수 없을 때의 처리
                model.addAttribute("errorMessage", "사용자 정보를 찾을 수 없습니다.");
                return "main";
            }
        }
        return "myPageModify";
    }


    /* 특정 유저 수정 작업 */
    @PostMapping("/useredit")
    public String updateProfilePicture(@RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = authentication.getName();
        User user = userService.findByEmail(email); // findByEmail 메서드 내에서 예외를 던지도록 처리

        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = imageUpload.uploadImage(imageFile, user.getUserId(), "user");
            user.setUserprofile(imagePath);
            userService.updateUser(user);
        }

        return "redirect:/myPageModify";
    }


    @PostMapping("/myPageModify")
    public String updatePassword(@ModelAttribute("joinForm2") JoinForm2 joinForm2,
                                 @RequestParam(name = "email") String email,
                                 Model model) {
        // 새 비밀번호가 null이거나 비어 있으면 리턴
        String newPassword1 = joinForm2.getNewPassword1();
        String newPassword2 = joinForm2.getNewPassword2();
        User currentUser = userService.getUserByEmail(email);
        String newNickname = joinForm2.getNickname();




        // 주어진 이메일 파라미터를 기반으로 사용자를 찾음

        if (currentUser == null) {
            model.addAttribute("updateResult", "해당 이메일의 사용자를 찾을 수 없습니다.");
            model.addAttribute("email", email);  // 실패한 경우 이메일 값을 다시 모델에 추가
            return "updatePw";
        }

        // 닉네임만 변경
        if (newNickname != null && !newNickname.trim().isEmpty() && newPassword1.equals("") && newPassword2.equals("")) {
            currentUser.setNickname(newNickname);
            userService.saveOrUpdate(currentUser);
            model.addAttribute("nicknameUpdateResult", "닉네임이 성공적으로 변경되었습니다.");
            SecurityContextHolder.getContext().setAuthentication(null);
            System.out.println("테스트1");
            // 로그인 페이지로 리다이렉트
            return "redirect:/login?passwordChanged=true";
        } else {
            // 비밀번호의 정보가 빈칸이 아니고, 닉네임 또한 null이 아닌경우
            if (newPassword1.equals(newPassword2)) {
                // 새 비밀번호와 새 비밀번호 확인이 일치하는 경우
                String hashedPassword = passwordEncoder.encode(newPassword1);
                currentUser.setPassword(hashedPassword);
                if(newNickname != null && !newNickname.trim().isEmpty()){
                    currentUser.setNickname(newNickname);
                }
                System.out.println("테스트2");
                // DB에 변경된 사용자 정보 저장
                userService.saveOrUpdate(currentUser);

                // 현재 인증 세션 종료
                SecurityContextHolder.getContext().setAuthentication(null);

                // 로그인 페이지로 리다이렉트
                return "redirect:/login?passwordChanged=true";
            } else {
                // 새 비밀번호와 새 비밀번호 확인이 일치하지 않는 경우
                model.addAttribute("updateResult", "비밀번호가 일치하지 않습니다.");
                model.addAttribute("email", email);  // 실패한 경우 이메일 값을 다시 모델에 추가
                return "redirect:/myPageModify";
            }
        }




    }







}


