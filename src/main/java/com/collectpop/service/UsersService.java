package com.collectpop.service;

import com.collectpop.domain.StoreReject;
import com.collectpop.domain.StoreRequestVO;
import com.collectpop.domain.UserMap;
import com.collectpop.domain.Users;
import com.collectpop.dto.Pager;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.userdetails.User;

import java.util.HashMap;
import java.util.List;

public interface UsersService {


    //로그인
    Users login(Users users);

    //회원가입 시 사용할 유저맵 데이터 가져오기
    List<UserMap> getUserMap( );

    //pk값으로 유저찾기
    Users findById(Long userId);

    //이메일로 아이디찾기
    Users findbyIdWithEmail(String email);

    //아이디로 유저찾기
    Users findEmailandName(@Param("username") String username, @Param("email") String email);

    //마이페이지 유저정보수정
    Users update(Users users);

    //비밀번호 재설정
    Users updatePw(Users users);

    //랜덤닉네임적용
    Users upRanNick(String code);

    //유저 비활성화
    void offline(Users users);
    //유저 활성화
    void online(Users users);

    //아이디 찾아서 중복검사
    boolean findUsername(String username);

    //회원가입
    void signupImg(Users users);

    //카카오 로그인 토큰값 받기
    String getAccessToken(String authorize_code) throws Throwable;
    //사용자 정보 토큰값으로 받기
    HashMap<String, Object> getUserInfo(String access_Token) throws Throwable;

    // 사용자,관리자 변경
    void changeAccess(Long userId);
    // 회원,스토어매니저 변경
    void changeStore(Long userId);
    // 온라인,비활성화 변경
    void changeOnline(Long userId);
    // 구독, 비구독 변경
    void changeRead(Long userId);

    //유저 전부 가져오기
    List<Users> getAllUsers(Pager pager);
    //유저 총합계
    Long getTotalUsers(Pager pager);
    ///내가 신청한 스토어요청 가져오기
    List<StoreRequestVO> getMyStoreRequest(Long userId);

    String getuserImg(String userName);

    String getUserName(String userName);

    Long getUserId(String userName);

    String getUserNickName(Long userId);

    String getUserIdofuserImg(Long userId);

    List<Users> getUsersByNewest();



    List<StoreRequestVO> AllRequest(Long userId);

    List<StoreRequestVO> getmyReq(Long userId);

    List<StoreReject> getReject(Long userId);
}
