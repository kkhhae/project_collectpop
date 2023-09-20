package com.collectpop.domain;

import lombok.Data;

@Data
public class StorePhotosVO {
    private Long photoId;    // 사진 고유번호
    private Long psId;       // 팝업스토어 고유번호
    private String photoPath; // 추가 이미지 경로
}
