package com.collectpop.domain;

import lombok.Data;

@Data
public class UserImg {

    private Long uiid;    // 프로필사진 고유번호 pk
    private Long userId;       // 회원 고유번호
    private String imgPath; // 추가 이미지 경로

    //userImg
    private Users users;


}
