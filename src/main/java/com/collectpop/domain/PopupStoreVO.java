package com.collectpop.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class PopupStoreVO {

    private Long psId; // 스토어 고유번호
    private String name; // 스토어 이름
    private String thumbnail; // 스토어 대표사진 경로
    private String content; // 상세 내용
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate; // 시작 날짜
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate; // 종료 날짜
    private LocalDateTime storeReg;	// 스토어 등록일
    private Long fee; // 입장료

    private Long storeLike; // 좋아요수
    private Long views; // 조회수
    private Long status; // 스토어 오픈상태
    private Long userId; // 멤버 고유번호

    private List<String> hashtags; // 스토어의 해시태그 목록
    private List<StorePhotosVO> additionalPhotos; // 스토어 추가 이미지 목록

    // MAP
    private Double latitude; // 위도
    private Double longitude; // 경도
    private String address; // 주소 (도로명주소)


}
