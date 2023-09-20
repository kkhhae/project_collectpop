package com.collectpop.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedReply {
    private Long frid;
    private Long fid;
    private Long userId;
    private String content;
    private LocalDateTime regDate;
}
