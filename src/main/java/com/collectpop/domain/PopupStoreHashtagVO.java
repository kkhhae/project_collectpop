package com.collectpop.domain;

import lombok.Data;

@Data
public class PopupStoreHashtagVO {
    private Long hashId; // 해시태그 ID
    private Long psId;   // 팝업스토어 ID
    private String hashtag; // 해시태그
}
