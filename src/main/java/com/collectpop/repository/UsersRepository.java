package com.collectpop.repository;

import com.collectpop.domain.StoreReject;
import com.collectpop.domain.StoreRequestVO;
import com.collectpop.domain.UserMap;
import com.collectpop.domain.Users;
import com.collectpop.dto.Pager;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UsersRepository {

    //유저네임으로 유저찾기
    Users findByUsername(String username);

    //회원가입처리
    Users signup(Users users);

    //회원가입 시 사용할 유저맵 데이터 가져오기
    List<UserMap> getUserMap( );

    //pk로 회원정보 찾기
    Users findById(Long userId);

    //email로 회원정보 찾기
    Users findByIdWithEmail(String email);

    //username으로 회원정보 찾기
    Users findEmailandName(@Param("username") String username, @Param("email") String email);

    //유저 업데이트
    Users update(Users users);

    //비밀번호 재설정
    Users updatePw(Users users);

    //유저이름(아이디) 중복검사
    Users findUsername(String username);

    //유저 비활성화
    void updateOnline(Users users);

    //랜덤닉네임
    Users upRanNick(String code);

    //회원정보 전부
    List<Users> getAllUsers(Pager pager);
    //회원 합계
    Long getTotalUsers(Pager pager);

    //사용자,관리자 변경
    void changeAccess(Long userId, String role_access);
    //회원,스토어매니저 변경
    void changeStore(Long userId, String role_store);
    //온라인, 비활성화
    void changeOnline(Long userId, String role_online);
    //구독,비구독
    void changeRead(Long userId, String role_read);

    //내가 신청한 스토어요청 가져오기
    List<StoreRequestVO> getMyStoreRequest(Long userId);
    String getUserName(String userName);

    Long getUserId(String userName);

    String getuserImg(String userName);

    String getUserNickName(Long userId);

    String getUserIdofuserImg(Long userId);

    List<Users> getUsersByNewest();

    //신청 가져오기
    List<StoreRequestVO> getmyReq(Long userId);

    List<StoreReject> getReject(Long userId);


}
