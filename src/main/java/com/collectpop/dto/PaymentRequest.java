package com.collectpop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime; // LocalDateTime import 추가

@Getter
@Setter
@ToString
public class PaymentRequest {
    private String merchantUid;
    private String productName;
    private int amount;
    private String buyerEmail;
    private String buyerName;
    private LocalDateTime paymentDateTime; // 결제일시 필드 추가
}
