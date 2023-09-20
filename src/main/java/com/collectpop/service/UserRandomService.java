package com.collectpop.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserRandomService {

    // 랜덤 인증 코드 전송
    public String createName() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();


        for (int i = 0; i < 6; i++) { // 6자리 이름
            int index = rnd.nextInt(2); // 0~1 까지 랜덤, rnd 값에 따라서 아래 switch 문이 실행됨
            char[] koreanChars = {'\uAC00', '\uB098', '\uB2E4', '\uB77C', '\uBC14'};  // '가', '나', '다', '라', '마'

            int randomIndex = rnd.nextInt(koreanChars.length);  // 배열의 길이 범위 내에서 랜덤 인덱스를 생성
            char randomChar = koreanChars[randomIndex];  // 랜덤하게 선택된 문자


            switch (index) {
                case 0:
                    key.append(randomChar);
                    break;
                case 1:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }

        return key.toString();
    }
}
