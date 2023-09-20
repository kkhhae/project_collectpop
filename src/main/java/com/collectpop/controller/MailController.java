package com.collectpop.controller;

import com.collectpop.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Properties;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/collectpop/users/")
public class MailController {

    private final MailService mailService;

    //이메일 인증
    @PostMapping("login/mailConfirm")
    @ResponseBody
    public String mailConfirm(@RequestParam("email") String email) throws Exception {
        try {
            String code = mailService.sendSimpleMessage(email);
            System.out.println("인증코드 : " + code);
            return code;
        } catch (Exception e) {
            e.printStackTrace();  // 콘솔에 오류를 출력
            return "오류! 이메일을 정확히 입력해주세요";
        }
    }



}
