package com.clipclap.rego.service;

import com.clipclap.rego.validation.JoinForm;

public interface AuthService {

    String join(JoinForm loginForm);
}
