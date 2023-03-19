package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//
//@RequiredArgsConstructor
//@Configuration
//@EnableWebSecurity // 스프링 시큐리티 필터(해당 클래스)가 스프링 필터체인에 등록이 됩니다.
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화 , preAuthorize, postAuthorize 어노테이션 활성화
public class SecurityConfig {

//    @Bean // 해당 메소드의 리턴되는 오브젝트는 IoC로 등록해준다.
//    public BCryptPasswordEncoder encodePwd() {
//        return new BCryptPasswordEncoder();
//    }
//
//    private final OauthService oauthService;
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//        http.authorizeRequests()
//                .antMatchers("/user/**").authenticated() // 인증만 되면 들어 갈 수 있는 주소
//                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
//                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
//                .anyRequest().permitAll()
//                .and()
//                .formLogin()
//                .loginPage("/loginForm")
//                // .usernameParameter("사용자설정 값) 바꿔줄 수 있음
//                .loginProcessingUrl("/login") //login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행해줍니다.
//                .defaultSuccessUrl("/")
//                .and()
//                .oauth2Login()
//                .loginPage("/loginForm") // 구글 로그인이 완료된 뒤의 후 처리가 필요함
//                // 1. 코드받기(인증) 2. 엑세스 토큰(권한)
//                // 3. 사용자 프로필 정보를 가져와서 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
//                // 4-2 추가적인 정보들과 합쳐서 회원가입을 진행하기도함
//                // 이때 구글 로그인은완료되면 코드가 아닌 엑세스토큰 + 사용자프로필 정보를 한번에 반환
//                .userInfoEndpoint()
//                .userService(oauthService);
//
//
//    }
}