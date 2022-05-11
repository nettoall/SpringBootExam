package com.nettoall.springboot.config.auth;

import com.nettoall.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity  // Spring security 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().headers().frameOptions().disable() // h2-console 화면을 사용하기 위해 disable
                .and()
                .authorizeRequests()// URL별 권환관리 설정하는 옵션의 시작점 (선언되야 antMatcher 사용 가능)
                .antMatchers("/","/css/**","/images/**",
                        "/js/**", "/h2-console/**").permitAll()
                .antMatchers("/api/v1/**").hasRole(Role.USER.name()) // URL, HTTP 메소드별 관리 가능
                .anyRequest().authenticated()  // 설정된 값들 이와 나머지 URL 인증된 사용자들에게만 허용
                .and()
                .logout()
                .logoutSuccessUrl("/")  // 로그아웃 기능에 대한 설정 시작
                .and()
                .oauth2Login()//OAuch 2 로그인 기능 시작점
                .userInfoEndpoint()// 로그인  성공 이후 사용자 정보를 가져올 때의 설정 담당
                .userService(customOAuth2UserService);  // 소셜 로그인 성공시 후속 조치를 진행할 UserService 인터페이스의 구현체 등록
    }
}
