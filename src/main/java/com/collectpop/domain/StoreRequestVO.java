package com.collectpop.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StoreRequestVO {
    private Long requestId; // 등업신청 고유번호
    private String companyname; // 회사명 (업체명)
    private String bossname; // 대표자명
    private String content; // 신청내용
    private String businessregistrationnumber; // 사업자등록번호
    private String contactnumber; // 신청자 연락처
    private String document1; // 파일, 사진 경로1
    private String document2; // 파일, 사진 경로2
    private String document3; // 파일, 사진 경로3
    private LocalDateTime requestReg ;	// 등업신청일


    private Long userId; // 멤버 고유번호
    private String email; // 유저 이메일
    private Long rejectStatus;

    private Long status;    //reject에서 사용할거 0=대기, 1=승인, 2=반려
    private String setStatusShow; //mylist에 보여줄 값 승인됨,반려됨


}
