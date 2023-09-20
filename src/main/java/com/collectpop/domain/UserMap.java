package com.collectpop.domain;

import lombok.Data;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

//회원 지역(서울,경기 등)
@Data
public class UserMap {

    private Long umId;          //pk
    private Long ulatitude;     //위도
    private Long ulongitude;    //경도
    private Long umaplevel;     //맵크기
    private String state;       //지역이름

}