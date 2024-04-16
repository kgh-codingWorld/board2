package org.zerock.board.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Log4j2
@Service
// 게시물 목록은 로그인 여부에 관계 없이 볼 수 있지만 게시물의 글쓰기는 로그인한 사용자만 접근 등...
// prePostEnabled: 원하는 곳에 @PreAuthorize 혹은 @PostAuthorize 어노테이션을 이용해 사전 혹은 사후 권한 체크 가능
@EnableMethodSecurity(prePostEnabled = true)
public class CustomUserDetailsService implements UserDetailsService {
    // UserDetailsService: 실제 인증을 처리함, username이라고 부르는 사용자의 아이디 인증을 코드로 구현함

    private PasswordEncoder passwordEncoder;

    public CustomUserDetailsService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // UserDetails: 사용자 인증과 관련된 정보들을 저장함, 시큐리티는 내부적으로 이 타입의 객체로 패스워드와 사용자 권한을 검사 및 확인

        log.info("loadUserByUsername: "+username);

        UserDetails userDetails = User.builder()
                .username("user1")
                //.password("1111")
                .password(passwordEncoder.encode("1111")) // 패스워드 인코딩(암호화)
                .authorities("ROLE_USER")
                .build();

        return userDetails;
    }
}
