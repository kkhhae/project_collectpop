package com.collectpop.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Users {



    private Long userId;            //pk
    private String email;           //이메일
    private String username;        //username으로 변경 <--id로 사용
    private String nickname;        //사용자의 이름
    private String password;        //비밀번호
    private String address;         //주소(지역 서울, 경기 등)
    private String phoneNum;        //폰번
    private LocalDateTime regDate;  //가입일   sysdate
    private Long repCount;          //신고횟수  default 0

    //업로드 추가
    private String img;                 //이미지 이름

    //권한 추가
    private Role role_access;       //룰 추가, 사용자, 관리자
    private Role role_store;        //룰 추가, 회원, 상인
    private Role role_online;       //룰 추가, 온라인, 비활성화
    private Role role_read;         //룰 추가, 비구독, 구독


}
