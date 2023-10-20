package com.clipclap.rego.service;

import com.clipclap.rego.model.dto.AdminUserDTO;
import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.validation.JoinForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthService {

    // 가입
    String join(JoinForm loginForm);

    // 특정 유저정보 가져오기 (매개변수 email)
    User getUserByEmail(String email);

    // 특정 유저정보 가져오기 (매개변수 username - username은 API통해서 가져와지는 정보가 저장되어있음)
    User getUser(String username);

    // 닉네임 죽복 체크
    String nicknameDuplicateCheck(String nickname);

    Page<User> getMemberList(Pageable pageable);

    User getUserDetail(Integer userId);

    void AdminUserEdit(AdminUserDTO adminUserDTO, Integer userId);

    void deleteUser(Integer userId);

    void deleteUsers(List<Integer> userIds);

    // 카카오 연결 끊기
    void kakaoUnlink(String accessToken);

    // 구글 연결 끊기
    void googleUnlink(String accessToken);
}
