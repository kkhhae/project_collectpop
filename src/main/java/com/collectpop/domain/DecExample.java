package com.collectpop.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DecExample {
    private Long dleid;
    private String dleContent;
    private LocalDateTime regDate;
}
