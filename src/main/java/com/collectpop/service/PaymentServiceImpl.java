package com.collectpop.service;

import com.collectpop.domain.Payment;
import com.collectpop.repository.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentMapper paymentMapper;

    @Override
    public void savePayment(Payment payment) {
        paymentMapper.savePayment(payment);
    }
}