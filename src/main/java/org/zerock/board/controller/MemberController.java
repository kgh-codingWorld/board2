package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member/")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    // 로그인 과정에 문제가 생기거나 로그아웃 처리할 때 사용
    @GetMapping("/login")
    public void loginGET(String error, String logout){
        log.info("login get...");
        log.info("logout: " + logout);
    }
}
