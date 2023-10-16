package com.clipclap.rego.service;

import com.clipclap.rego.model.dto.AdminUserDTO;
import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.validation.JoinForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthService {

    String join(JoinForm loginForm);

    User getUserByEmail(String email);

    User getUser(String username);


    String nicknameDuplicateCheck(String nickname);

    Page<User> getMemberList(Pageable pageable);

    User getUserDetail(Integer userId);

    void AdminUserEdit(AdminUserDTO adminUserDTO, Integer userId);

    void deleteUser(Integer userId);

    void deleteUsers(List<Integer> userIds);

    void kakaoUnlink(String accessToken);

    void googleUnlink(String accessToken);
}
