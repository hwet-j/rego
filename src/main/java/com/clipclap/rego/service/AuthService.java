package com.clipclap.rego.service;

import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.validation.JoinForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthService {

    String join(JoinForm loginForm);

    User getUserByEmail(String email);

    User getUser(String username);


    String nicknameDuplicateCheck(String nickname);

    Page<User> getMemberList(Pageable pageable);
}
