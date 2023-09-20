package com.collectpop.controller;

import com.collectpop.domain.Payment;
import com.collectpop.domain.Role;
import com.collectpop.domain.Users;
import com.collectpop.dto.PaymentRequest;
import com.collectpop.service.PaymentService;
import com.collectpop.service.UsersService;
import com.collectpop.security.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/collectpop/payment/*")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final UsersService usersService;
    private final PaymentService paymentService;

    @GetMapping("")
    public String payment(@AuthenticationPrincipal CustomUser customUser, Model model) {
        Users users = customUser.getUsers();
        model.addAttribute("users", users);
        log.info("유저 객체 정보 : {}", users);
        return "subscribe/payment";
    }

    @PostMapping("processPayment")
    public ResponseEntity<Map<String, String>> processPayment(@RequestBody PaymentRequest paymentRequest,
                                                              @AuthenticationPrincipal CustomUser customUser,
                                                              Model model) {
        try {
            Users users = customUser.getUsers();

            // PaymentRequest에서 필요한 정보를 추출하여 Payment 객체로 변환
            Payment payment = new Payment();
            payment.setMerchantUid(paymentRequest.getMerchantUid());
            payment.setProductName(paymentRequest.getProductName());
            payment.setAmount(paymentRequest.getAmount());
            payment.setBuyerEmail(users.getEmail());
            payment.setBuyerName(users.getUsername());
            payment.setPaymentStatus("paid");
            payment.setPaymentDateTime(LocalDateTime.now());

            log.info("payment : {} ", payment);

            // PaymentService를 사용하여 결제 정보를 저장
            paymentService.savePayment(payment);

            // 저장 성공 응답
            Map<String, String> response = new HashMap<>();
            response.put("success", "true");

            Users users1 = customUser.getUsers();
            log.info("userName : {}", users1.getUsername());
            log.info("userName2 : {}", payment.getBuyerName());
            if (users1.getUsername() == payment.getBuyerName()) {
                users1.setRole_read(Role.SUB);
                usersService.changeRead(users1.getUserId());
                // 역할이 변경된 경우 로그 메시지 출력
                log.info("User {} has been assigned the ROLE_READ_SUB role.", users1.getUsername());
            }

            // Spring Security 컨텍스트에 업데이트된 UserDetails를 저장
            CustomUser updatedCustomUser = new CustomUser(users1);
            Authentication authentication = new UsernamePasswordAuthenticationToken(updatedCustomUser, customUser.getPassword(), updatedCustomUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // 저장 실패 응답
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("success", "false");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
