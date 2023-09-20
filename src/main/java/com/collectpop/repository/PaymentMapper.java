package com.collectpop.repository;

import com.collectpop.domain.Payment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {
   public void savePayment(Payment payment);
}
