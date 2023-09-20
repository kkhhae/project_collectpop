package com.collectpop.domain;

import lombok.Data;

@Data
public class StoreReject {

    private Long rejectId;
    private String content;
    private Long status;

    //스토어 리퀘스트 고유번호
    private Long requestId;
    //사용자 고유번호
    private Long userId;

}
