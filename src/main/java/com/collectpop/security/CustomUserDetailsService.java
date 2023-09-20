package com.collectpop.security;

import com.collectpop.domain.Users;
import com.collectpop.repository.UsersMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersMapper usersMapper;

    //시큐리티 사용을 위해 무조건 구현해야 함
    //시큐리티가 로그인처리해주고, 로그인 시 자동으로 호출되는 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users findUser = usersMapper.findByUsername(username);

        if(findUser == null){
            //throws new UsernameNotFoundException("해당 사용자가 존재하지 않습니다." + username);
        }

        return new CustomUser(findUser);
    }



}
