package com.collectpop.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class Feed {
    private Long fid;
    private Long userId;
    private String content;
    private Long likes;
    private Long views;
    private LocalDateTime regDate;

}
