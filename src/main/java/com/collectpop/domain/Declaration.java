package com.collectpop.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Declaration {
    private Long did;
    private Long fid;
    private Long userId;
    private Long dleid;
    private Long decnum;
    private LocalDateTime regDate;
}
