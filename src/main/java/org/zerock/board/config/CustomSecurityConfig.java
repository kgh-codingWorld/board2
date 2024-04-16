package org.zerock.board.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class CustomSecurityConfig {
    // 시큐리티는 단순히 application.properties를 이용하는 것보다 코드를 이용해 설정을 조정하는 경우가 더 많음
    // 브라우저에서 /board/list를 호출하면 /login 경로로  -> 로그인창 자동 생성
    // 로그인하지 않아도 볼 수 있도록 설정하려면 개발자가 직접 설정하는 코드가 반드시 존재해야 함 -> SecurityFilterChain

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // 자동 생성되는 로그인창 건너뛰기용

        log.info("-------------configure-------------");

        // 6.1 버전에서 제외 됨 (스프링 3.0이후 버전에서는 사용 안됨)
        // http.formLogin().loginPage("/member/login");
        // http.formLogin(Customizer.withDefaults());
        // 람다식으로 사용할 것을 권고 함. 아래로 변경
        http.formLogin(form -> {
            form.loginPage("/member/login"); // 694 추가 로그인 폼 추가 .loginPage("/member/login");
            // member/login.html을 찾음.
        });  // CustomUserDetailsService 클래스로 UserDeTailsService 구현체로 생성 // 로그인 화면에서 로그인을 진행하겠다는 설정

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // 완전히 정적으로 동작하는 파일에 시큐리티 적용할 필요 없으므로 제외시킴(css 파일을 호출할 때 필터 미작동)

        log.info("-------------web configure-------------");

        return (web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
    }
}
