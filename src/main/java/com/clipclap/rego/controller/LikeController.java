package com.clipclap.rego.controller;

import com.clipclap.rego.model.entitiy.LikeAttraction;
import com.clipclap.rego.repository.UserRepository;
import com.clipclap.rego.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/likeAttraction")
public class LikeController {

    private final LikeService likeService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // tourAttraction 에 대해서 좋아요가 눌려있는지에 대해 응답해주는 controller
    @GetMapping("/checkLike")
    @ResponseBody
    public String checkLike(@RequestParam("attractionId") Integer attractionId){
        //이메일 정보를 받아온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String userEmail;

        if (principal instanceof UserDetails) {
            userEmail = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            userEmail = (String) principal; // 여기서는 principal이 String이므로, 바로 사용자 이메일(또는 이름)으로 사용합니다.
        } else {
            throw new IllegalArgumentException("Principal can not be recognized!");
        }

        int likeCnt = likeService.countByAttactionIdAndUserEmail(attractionId,userEmail);
        if (likeCnt!=1 || userEmail==null || userEmail.isEmpty()){
            return "beforeLike";
        }else {
            return "afterLike";
        }

    }

    @PostMapping("/toggleLike")
    @ResponseBody
    public String toggleLike(@RequestParam("attractionId") Integer attractionId, Principal principal){
        String userEmail = principal.getName(); // 현재 사용자의 이메일을 가져옵니다.

        // DB에서 현재 '좋아요' 상태를 조회하고 상태를 변경합니다. (이 로직은 해당 서비스 구현에 따라 다를 수 있습니다.)
        boolean isLiked = likeService.toggleLike(attractionId, userEmail);

        // 새로운 '좋아요' 상태에 따라 응답을 반환합니다.
        return isLiked ? "success" : "fail";
    }

    // tourAttraction 좋아요 버튼
    @GetMapping("/addLike")
    @ResponseBody
    public ResponseEntity<String> addLike(@RequestParam("attractionId") Integer attractionId) {
        // 현재 인증된 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = "";
        // 사용자의 이메일 가져오기 (사용자가 UserDetails를 구현한 경우)
        if (1==1) {
            System.out.println("if문 돌파");
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            userEmail = userDetails.getUsername(); // 이메일 또는 원하는 다른 사용자 정보를 가져올 수 있습니다.

            // userEmail을 사용하여 원하는 작업 수행
        } else {
            ResponseEntity.ok("user Not Found");
        }
        String message = likeService.addLike(userEmail, attractionId);
        return ResponseEntity.ok(message);
    }



    // 사용자의 모든 좋아요 조회
    @GetMapping("/userLikes")
    public ResponseEntity<List<LikeAttraction>> getUserLikes(@RequestParam("userEmail") String userEmail) {
        List<LikeAttraction> userLikes = likeService.getUserLikes(userEmail);
        return ResponseEntity.ok(userLikes);
    }

    // 특정 관광지에 대한 좋아요 조회
    @GetMapping("/attractionLikes")
    public ResponseEntity<List<LikeAttraction>> getAttractionLikes(@RequestParam("attractionId") Integer attractionId) {
        List<LikeAttraction> attractionLikes = likeService.getAttractionLikes(attractionId);
        return ResponseEntity.ok(attractionLikes);
    }
}
