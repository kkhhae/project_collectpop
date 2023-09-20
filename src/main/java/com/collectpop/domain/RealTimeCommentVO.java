package com.collectpop.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RealTimeCommentVO {
    private Long commentId; // 실시간 댓글 고유번호
    private Long psId; // 팝업 고유번호
    private Long userId; // 유저 고유번호
    private String nickname; // 유저 닉네임
    private String content; // 댓글 작성내용
    private LocalDateTime commentReg; // 댓글 등록 시간

}