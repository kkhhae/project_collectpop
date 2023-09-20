package com.collectpop.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime; // LocalDateTime import 추가

@Getter
@Setter
@ToString
public class Payment {
    private Long paymentId;
    private String merchantUid;
    private String productName;
    private Integer amount;
    private String buyerEmail;
    private String buyerName;
    private String paymentStatus;
    private LocalDateTime paymentDateTime; // 결제일시 필드 추가
}
