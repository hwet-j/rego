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

    // 좋아요 버튼
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
