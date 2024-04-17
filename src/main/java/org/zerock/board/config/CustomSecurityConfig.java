package org.zerock.board.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.zerock.board.security.CustomUserDetailsService;
import org.zerock.board.security.filter.APILoginFilter;
import org.zerock.board.security.filter.APILoginSuccessHandler;
import org.zerock.board.security.handler.Custom403Handler;
import org.zerock.board.security.handler.CustomSocialLoginSuccessHandler;
import org.zerock.board.service.APIUserDetailsService;

import javax.sql.DataSource;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class CustomSecurityConfig {
    // 시큐리티는 단순히 application.properties를 이용하는 것보다 코드를 이용해 설정을 조정하는 경우가 더 많음
    // 브라우저에서 /board/list를 호출하면 /login 경로로  -> 로그인창 자동 생성
    // 로그인하지 않아도 볼 수 있도록 설정하려면 개발자가 직접 설정하는 코드가 반드시 존재해야 함 -> SecurityFilterChain

    // remember-me
    private final DataSource dataSource;
    private final CustomUserDetailsService userDetailsService;

    private final APIUserDetailsService apiUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // 자동 생성되는 로그인창 건너뛰기용

        // AuthenticationManager 설정
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(apiUserDetailsService).passwordEncoder(passwordEncoder());

        // Get AuthenticationManager
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http.authenticationManager(authenticationManager);

        // APILoginFilter
        APILoginFilter apiLoginFilter = new APILoginFilter("/generateToken");
        apiLoginFilter.setAuthenticationManager(authenticationManager);

        APILoginSuccessHandler apiLoginSuccessHandler = new APILoginSuccessHandler();
        apiLoginFilter.setAuthenticationSuccessHandler(apiLoginSuccessHandler);

        // APILoginFilter의 위치 조정
        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);

        log.info("-------------configure-------------");

        // 6.1 버전에서 제외 됨 (스프링 3.0이후 버전에서는 사용 안됨)
        // http.formLogin().loginPage("/member/login");
        // http.formLogin(Customizer.withDefaults());
        // 람다식으로 사용할 것을 권고 함. 아래로 변경
        // 커스텀 로그인 페이지
        http.formLogin(form -> {
            form.loginPage("/member/login"); // 694 추가 로그인 폼 추가 .loginPage("/member/login");
            // member/login.html을 찾음.
        });  // CustomUserDetailsService 클래스로 UserDeTailsService 구현체로 생성 // 로그인 화면에서 로그인을 진행하겠다는 설정

        // username과 passwored 파라미터만으로 로그인 가능해짐
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable()); // 토큰 비활성화 -> 사용 시 post/put/delete를 이용하는 모든 코드를 수정해야 하는 단점

        http.rememberMe(httpSecurityRememberMeConfigurer -> {

            httpSecurityRememberMeConfigurer.key("12345678")
                    .tokenRepository(persistentTokenRepository())  // 하단에 메서드 추가
                    .userDetailsService(userDetailsService)
                    .tokenValiditySeconds(60*60*24*30);

        });

        // 718 추가 403 오류 처리
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
            httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler());
        });

        http.oauth2Login( httpSecurityOAuth2LoginConfigurer -> {
            httpSecurityOAuth2LoginConfigurer.loginPage("/member/login");
            httpSecurityOAuth2LoginConfigurer.successHandler(authenticationSuccessHandler()); // 761 추가 소설로그인 암호 강제 변경
        });

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new Custom403Handler();
    }

    @Bean // 760 추가 소설 회원가입 후 암호 설정
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }

    // remember-me
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // 완전히 정적으로 동작하는 파일에 시큐리티 적용할 필요 없으므로 제외시킴(css 파일을 호출할 때 필터 미작동)

        log.info("-------------web configure-------------");

        return (web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }
}
