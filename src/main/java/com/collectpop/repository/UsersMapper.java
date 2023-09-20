package com.collectpop.repository;

import com.collectpop.domain.StoreReject;
import com.collectpop.domain.StoreRequestVO;
import com.collectpop.domain.UserMap;
import com.collectpop.domain.Users;
import com.collectpop.dto.Pager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UsersMapper {

    //회원 가입
    int save(Users users);


    //username으로 유저찾기 + 아이디중복검사
    Users findByUsername(String username);

    //usermap가져오기
    List<UserMap> getUserMap( );

    //유저 id로 유저찾기
    Users findById(Long userId);

    //유저 이름으로 유저찾기
    Users getUsername(String username);

    //email로 회원정보 찾기(username)
    Users findByIdWithEmail(String email);

    //username으로 회원정보 찾기(password)
    Users findEmailandName(@Param ("username") String username, @Param("email") String email);

    //유저 정보 업데이트
    int update(Users users);

    //유저 비밀번호 재설정
    int updatePw(Users users);

    //유저 비활성화/활성화
    void updateOnline(Users users);

    //유저 닉네임 랜덤부여
    void upRanNick(String code);

    //유저 전부 가져오기
    List<Users> getAllUsers(Pager pager);
    //유저 총합계
    Long getTotalUsers(Pager pager);

    //사용자,관리자 변경
    void changeAccess(@Param("userId") Long userId, @Param("role_access") String role_access);
    //회원,스토어매니저 변경
    void changeStore(@Param("userId") Long userId, @Param("role_store") String role_store);
    //온라인, 비활성화 변경
    void changeOnline(@Param("userId") Long userId,@Param("role_online") String roleOnline);
    //비구독, 구독 변경
    void changeRead(@Param("userId") Long userId,@Param("role_read") String roleRead);

    String getUserNickName(String userName);

    Long getUserId(String userName);

    String getuserImg(String userName);

    String getUserIdOfUserNickName(Long userId);

    String getUserIdofuserImg(Long userId);

    List<StoreRequestVO> getMyStoreRequest(Long userId);

    List<Users> getUsersByNewest();

    //요청 가져오기
    List<StoreRequestVO> getmyReq(Long userId);

    List<StoreReject> getReject(Long userId);


}
