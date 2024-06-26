package org.zerock.board.security.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.board.security.dto.MemberSecurityDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {
    // 카카오톡 회원가입 후 암호가 1111 임으로 강제로 변경 하는 코드
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("----------------------------------------------------------");
        log.info("CustomLoginSuccessHandler.onAuthenticationSuccess() 메서드 실행 ..........");
        log.info(authentication.getPrincipal());

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();

        String encodedPw = memberSecurityDTO.getMpw();

        //소셜로그인이고 회원의 패스워드가 1111이라면
        if (memberSecurityDTO.isSocial()
                && (memberSecurityDTO.getMpw().equals("1111")
                ||  passwordEncoder.matches("1111", memberSecurityDTO.getMpw())
        )) {
            log.info("Should Change Password");

            log.info("Redirect to Member Modify ");
            response.sendRedirect("/member/modify");

            return;
        } else {

            response.sendRedirect("/board/list");
        }
    }

}