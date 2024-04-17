package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.MemberJoinDTO;
import org.zerock.board.service.MemberService;

@Controller
@RequestMapping("/member/")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    // 의존성 주입
    private final MemberService memberSerivce;

    // 로그인 과정에 문제가 생기거나 로그아웃 처리할 때 사용
    @GetMapping("/login")
    public void loginGET(String error, String logout){
        log.info("login get...");
        log.info("logout: " + logout);

        if(logout != null) {
            log.info("user logout...");
        }
    }

    @GetMapping("/join")
    public void joinGET() {
        log.info("join get...");
    }

    @PostMapping("/join")
    public String joinPost(MemberJoinDTO memberJoinDTO, RedirectAttributes rttr) {
        log.info("join post...");
        log.info(memberJoinDTO);

        try {
            memberSerivce.join(memberJoinDTO);
        } catch (MemberService.MidExistException e) {
            rttr.addFlashAttribute("error", "mid");
            return "redirect:/member/join";
        }
        rttr.addFlashAttribute("result", "success");
        return "redirect:/member/login"; // 회원가입 후 로그인 페이지로 이동
    }
}
