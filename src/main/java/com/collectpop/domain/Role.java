package com.collectpop.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

//사용자 권한 Enum
@RequiredArgsConstructor
@Getter
public enum Role {

    //사용자, 관리자
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN"),
    //회원, 스토어매니저
    MEMBER("ROLE_MEMBER"),STOREMANAGER("ROLE_STOREMANAGER"),
    //활성화, 비활성화
    ACTIVE("ROLE_ACTIVE"), DISABLED("ROLE_DISABLED"),
    //구독자, 비구독자
    SUB("ROLE_SUBSCRIBE"), NONSUB("ROLE_NONSUB"),

    ;

    private final String value;
}
