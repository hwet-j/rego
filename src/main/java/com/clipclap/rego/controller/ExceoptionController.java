package com.clipclap.rego.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice(annotations = Controller.class)
public class ExceoptionController {

    @ExceptionHandler(AccessDeniedException.class)
    public String handlerAccessDeniedException() {
        return "error";
    }
}
