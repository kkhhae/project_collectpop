package com.collectpop.config;

import com.collectpop.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration //스프링 빈으로, 스프링 환경설정파일 등록
@EnableWebSecurity //모든 요청 url은 스프링 시큐리티의 제어를 받도록 해주는 어노테이션
@RequiredArgsConstructor
public class SecurityConfig  {

    //일반 사용자 로그인 및 설정
    private final CustomUserDetailsService userDetailsService; //remember me기능 사용할 때 필요함
    private final DataSource dataSource; //remember-me를 db에 저장하기 위해 필요




    //HttpSecurity (구 : configure(HttpSecurity http))
    //시큐리티 접근 제한, 로그인, 로그아웃 등 http관련 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.cors()
                .and()

                .authorizeRequests()
                .antMatchers("/collectpop/users/mypage/**").hasAuthority("ROLE_ACTIVE")  //인증(로그인)된 사용자만 접근가능페이지 설정하기
                .antMatchers("/collectpop/users/edit/**").hasAuthority("ROLE_ACTIVE")
                .antMatchers("/collectpop/store/addStore/**").hasAuthority("ROLE_STOREMANAGER") //스토어매니저만 스토어작성가능
                .antMatchers("/collectpop/store/editStore/**").hasAuthority("ROLE_STOREMANAGER") //스토어매니저만 스토어작성가능
                .antMatchers("/collectpop/payment/**").hasAuthority("ROLE_ACTIVE") //스토어매니저만 스토어작성가능
                .antMatchers("/collectpop/admin/decexmanaged/**").hasAuthority("ROLE_ADMIN") //ROLE_ADMIN이라는 룰 가진 사용자만 admin 접속가능
                .antMatchers("/collectpop/admin/declaration/**").hasAuthority("ROLE_ADMIN") //ROLE_ADMIN이라는 룰 가진 사용자만 admin 접속가능
                .antMatchers("/collectpop/admin/mainm/**").hasAuthority("ROLE_ADMIN") //ROLE_ADMIN이라는 룰 가진 사용자만 admin 접속가능
                .antMatchers("/collectpop/admin/rejectRequest/**").hasAuthority("ROLE_ADMIN") //ROLE_ADMIN이라는 룰 가진 사용자만 admin 접속가능
                .antMatchers("/collectpop/admin/requestDetail/**").hasAuthority("ROLE_ADMIN") //ROLE_ADMIN이라는 룰 가진 사용자만 admin 접속가능
                //.antMatchers("/collectpop/admin/userList/**").hasAuthority("ROLE_ADMIN") //ROLE_ADMIN이라는 룰 가진 사용자만 admin 접속가능
                .antMatchers("/collectpop/users/myList/**").hasAuthority("ROLE_ACTIVE") //ROLE_ADMIN이라는 룰 가진 사용자만 admin 접속가능

                .anyRequest().permitAll() //나머지는 모든요청을 다 허용하겠다 설정

                //아이디 기억할때 사용
                .and()
                .rememberMe()
                .tokenRepository(tokenRepository()) //db에 접속해서 remember-me에 대한 정보 저장
                .userDetailsService(userDetailsService) //new Customer로 변경되서 날라올거 투입

                .and()

                //로그인
                .formLogin()
                .loginPage("/collectpop/users/login") //로그인 페이지 경로 설정
                .usernameParameter("username") //username <- email로 바꿀수있으면 하고 아니면 그냥 username을 id로
                .passwordParameter("password")
                .defaultSuccessUrl("/collectpop/") //로그인 성공 시 경로
                .failureUrl("/collectpop/error") //로그인 실패 시 경로
                .successHandler(new UserSuccessHandler()) //로그인성공시 인증연결 자동실행

                .and()

                //로그아웃
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true) //세션 날리기
                .clearAuthentication(true)  //시큐리티설정 접근 권한 날리기(sns) , 오류나면 주석처리
                .deleteCookies("remember-me") //쿠키날리기 , default : remember-me
                .logoutSuccessUrl("/collectpop/")

                .and()

                .csrf().disable();  //csrf(토큰) 사용안하겠다.


        return http.build();
    }

    //WebSecurity : 시큐리티 적용 안할 경로 지정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){ //css, js, 이미지파일 등
        return (web) -> web
                .ignoring()
                .antMatchers("/assets/**");


    }

    //AuthenticationManager : 시큐리티 인증 담당, UserDetailsService가 구현클래스, PasswordEncoder가 필요함.
    @Bean
    public AuthenticationManager authenticationManager
    (AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    //BCryptPassword : 비밀번호 암호화 해주는 클래스 빈으로 등록 (시큐리티에서 비밀번호 암호화 강제함.필수사용)
    //암호화된 비밀번호 자체는 복호화가 불가능함
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //데이터베이스 사용한 remember-me 적용 시, DB에 직접 접속해 insert,delete 처리 자동으로 해줌
    @Bean
    public PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource); //db접속하기

        return jdbcTokenRepository;
    }

}
