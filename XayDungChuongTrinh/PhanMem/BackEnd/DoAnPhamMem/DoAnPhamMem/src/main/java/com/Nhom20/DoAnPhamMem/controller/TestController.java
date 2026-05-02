package com.Nhom20.DoAnPhamMem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/")
    public String sayHello() {
        return "Hello Minh Huy";
    }

    @GetMapping("/api/test/secure")
    public String secureHello() {
        return "This is a secure message. You are authenticated!";
    }
}
