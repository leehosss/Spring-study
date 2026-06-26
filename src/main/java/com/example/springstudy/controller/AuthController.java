package com.example.springstudy.controller;

import com.example.springstudy.dto.RegisterForm;
import com.example.springstudy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    // 회원가입 페이지
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "auth/register";
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterForm form,
                           BindingResult bindingResult,
                           Model model) {

        // 유효성 검사 실패
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        // 중복 아이디 검사
        if (userService.existsByUsername(form.getUsername())) {
            model.addAttribute("errorMessage", "이미 사용 중인 아이디입니다.");
            return "auth/register";
        }

        userService.register(form.getUsername(), form.getPassword(), form.getNickname());
        return "redirect:/auth/login?registered=true";
    }
}
