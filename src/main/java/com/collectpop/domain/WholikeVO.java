package com.collectpop.domain;

import lombok.Data;

import java.time.LocalDateTime;

// 팝업스토어 좋아요 관련 VO
@Data
public class WholikeVO {

    private Long likeId;        // 좋아요 고유번호
    private Long psId;          // 팝업스토어 고유번호
    private Long userId;        // 사용자 고유번호
    private int likeStatus;     // 좋아요 상태  (1: 좋아요 0: 좋아요X)
}

