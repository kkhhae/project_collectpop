package com.collectpop.repository;

import com.collectpop.domain.StoreReject;
import com.collectpop.domain.StoreRequestVO;
import com.collectpop.domain.UserMap;
import com.collectpop.domain.Users;
import com.collectpop.dto.Pager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class UsersRepositoryImpl implements UsersRepository {

    private final UsersMapper usersMapper;


    @Override
    public Users findByUsername(String username) {
        return usersMapper.getUsername(username);
    }

    @Override
    public Users signup(Users users) {
        usersMapper.save(users);
        return users;
    }


    @Override
    public List<UserMap> getUserMap( ) {

        List<UserMap> userMap = usersMapper.getUserMap();
//        log.info("userMap : {}", userMap);
        return userMap;
    }

    @Override
    public Users findById(Long userId) {
        return usersMapper.findById(userId);
    }

    @Override
    public Users findByIdWithEmail(String email) {
        log.info("findEmail rep : {}", email);
        return usersMapper.findByIdWithEmail(email);
    }

    @Override
    public Users findEmailandName(@Param("username") String username, @Param("email") String email) {
        return usersMapper.findEmailandName(username, email);
    }

    @Override
    public Users update(Users users) {
        log.info("업데이트 repository 호출");
        usersMapper.update(users);
        log.info("user update : {}", users);
        return usersMapper.findById(users.getUserId());
    }

    @Override
    public Users updatePw(Users users) {
        log.info("비밀번호 재설정 rep 호출");
        log.info("user updatePw rep : {}", users);
        usersMapper.updatePw(users);
        return usersMapper.findById(users.getUserId());
    }

    @Override
    public Users findUsername(String username) {
        return usersMapper.findByUsername(username);
    }


    @Override
    public void updateOnline(Users users) {
        usersMapper.updateOnline(users);
    }

    @Override
    public Users upRanNick(String code) {
        usersMapper.upRanNick(code);
        return null;
    }

    @Override
    public List<Users> getAllUsers(Pager pager) {
        return usersMapper.getAllUsers(pager);
    }

    @Override
    public Long getTotalUsers(Pager pager) {
        return usersMapper.getTotalUsers(pager);
    }

    @Override
    public void changeAccess(Long userId, String role_access) {
        usersMapper.changeAccess(userId, role_access);
    }

    @Override
    public void changeStore(Long userId, String role_store) {
        usersMapper.changeStore(userId,role_store);
    }

    @Override
    public void changeOnline(Long userId, String role_online) {
        usersMapper.changeOnline(userId,role_online);
    }

    @Override
    public void changeRead(Long userId, String role_read) {
        usersMapper.changeRead(userId,role_read);
    }

    @Override
    public List<StoreRequestVO> getMyStoreRequest(Long userId) {
        return usersMapper.getMyStoreRequest(userId);
    }

    @Override
    public String getUserName(String userName) {
        return usersMapper.getUserNickName(userName);
    }

    @Override
    public Long getUserId(String userName) {
        return usersMapper.getUserId(userName);
    }

    @Override
    public String getuserImg(String userName) {
        return usersMapper.getuserImg(userName);
    }

    @Override
    public String getUserNickName(Long userId) {
        return usersMapper.getUserIdOfUserNickName(userId);
    }

    @Override
    public String getUserIdofuserImg(Long userId) {
        return usersMapper.getUserIdofuserImg(userId);
    }

    @Override
    public List<Users> getUsersByNewest() {
        return usersMapper.getUsersByNewest();
    }


    @Override
    public List<StoreRequestVO> getmyReq(Long userId) {
        return usersMapper.getmyReq(userId);
    }

    @Override
    public List<StoreReject> getReject(Long userId) {
        return usersMapper.getReject(userId);
    }


}
