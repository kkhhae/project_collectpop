package com.collectpop.service;

import com.collectpop.domain.*;
import com.collectpop.dto.ApiCode;
import com.collectpop.dto.Pager;
import com.collectpop.repository.UsersRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private String apikey = ApiCode.KAKAOLOGINCODE.getAbbreviation();
//    private final IACDAO

    //로그인
    @Override
    public Users login(Users users) {
        Users findUser = usersRepository.findByUsername(users.getUsername());
        if(findUser != null){
            if(findUser.getPassword().equals(users.getPassword())){
                return findUser;
            }
        }
        return null; // findUser이 없거나(username X) , 비번이 틀리면 null 리턴
    }

    //회원가입 시 사용할 유저맵 데이터 가져오기
    @Override
    public List<UserMap> getUserMap() {

        return usersRepository.getUserMap();
    }

    //id로 유저찾기
    @Override
    public Users findById(Long userId) {
        return usersRepository.findById(userId);
    }

    //email로 유저찾기
    @Override
    public Users findbyIdWithEmail(String email) {
        log.info("findEmail call");
        return usersRepository.findByIdWithEmail(email);
    }

    //username(id)랑 email로 유저찾기
    @Override
    public Users findEmailandName(@Param("username") String username, @Param("email") String email) {
        return usersRepository.findEmailandName(username,email);
    }

    //회원정보수정
    @Override
    public Users update(Users users) {
        return usersRepository.update(users);
    }


    //비밀번호 재설정
    @Override
    public Users updatePw(Users users) {
        //비밀번호암호화
        users.setPassword(passwordEncoder.encode(users.getPassword())); //사용자가 입력한 패스워드 꺼내와서 암호화

        return usersRepository.updatePw(users);
    }

    @Override
    public Users upRanNick(String code) {
        log.info("nickname 랜덤 호출");
        return usersRepository.upRanNick(code);
    }

    //유저 비활
    @Override
    public void offline(Users users) {
        users.setRole_online(Role.DISABLED);
        usersRepository.updateOnline(users);

    }
    //유저 활성
    @Override
    public void online(Users users) {
        users.setRole_online(Role.ACTIVE);
        usersRepository.updateOnline(users);
    }


    //아이디 중복검사용
    @Override
    public boolean findUsername(String username) {
        boolean result = false;
        Users userRe = usersRepository.findUsername(username);

        if(userRe == null) {
            result = true;
        }
        return result;
    }

    //회원가입 + 프사저장 + 룰 추가
    @Override
    public void signupImg(Users users) {
        //비밀번호암호화
        users.setPassword(passwordEncoder.encode(users.getPassword())); //사용자가 입력한 패스워드 꺼내와서 암호화

        //role추가
        users.setRole_access(Role.USER);   //사용자모드로 가입 <-> 관리자
        users.setRole_store(Role.MEMBER); //일반회원으로 가입 <-> 스토어매니저
        users.setRole_read(Role.NONSUB); //비구독상태로 가입 <-> 구독
        users.setRole_online(Role.ACTIVE); //온라인상태로 가입 <-> 비활성화

        log.info("signup ser user : {}", users);
        //회원정보 저장
        usersRepository.signup(users);
    }



    //카카오 로그인 access token 값 받기
    @Override
    public String getAccessToken(String authorize_code) throws Exception {
        log.info("call getAccess Token");
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // POST 요청을 위해 기본값이 false인 setDoOutput을 true로

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            // POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");

            //배포 및 공유 시 client_id는 비공개로
            sb.append("&client_id=").append(apikey); // REST_API키 본인이 발급받은 key 넣어주기
            sb.append("&redirect_uri=http://localhost:8080/collectpop/users/kakaoLogin"); // REDIRECT_URI 본인이 설정한 주소 넣어주기

            sb.append("&code=" + authorize_code);
            bw.write(sb.toString());
            bw.flush();

            // 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("getToken responseCode : " + responseCode);

            // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("ser response body : " + result);

            // jackson objectmapper 객체 생성
            ObjectMapper objectMapper = new ObjectMapper();
            // JSON String -> Map 변환
            Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
            });

            //토큰값 받아오기
            access_Token = jsonMap.get("access_token").toString();
            refresh_Token = jsonMap.get("refresh_token").toString();

            log.info("ser access_token : " + access_Token);
            log.info("ser refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return access_Token;
    }

    //로그인한 유저정보 가져오기
    @Override
    public HashMap<String, Object> getUserInfo(String access_Token) throws Throwable {
        log.info("call getUserInfo");
        // 요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
        HashMap<String, Object> userInfo = new HashMap<String, Object>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            int responseCode = conn.getResponseCode();
            log.info("getUserInfo responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response body : " + result);
            log.info("result type" + result.getClass().getName()); // java.lang.String

            try {
                // jackson objectmapper 객체 생성
                ObjectMapper objectMapper = new ObjectMapper();
                // JSON String -> Map
                Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
                });

                System.out.println(jsonMap.get("properties"));

                //각 이름으로 되어있는 값들 키값으로 데이터 뽑아오기
                Map<String, Object> properties = (Map<String, Object>) jsonMap.get("properties");
                Map<String, Object> kakao_account = (Map<String, Object>) jsonMap.get("kakao_account");

                //확인용
                System.out.println(properties.get("nickname"));
                System.out.println(kakao_account.get("email"));
                System.out.println(properties.get("profile_image"));
                System.out.println(jsonMap.get("id"));

                //카카오톡 정보 가져오기
                String nickname = properties.get("nickname").toString();
                String email = kakao_account.get("email").toString();
                String img = properties.get("profile_image").toString();
                Long id = (Long) jsonMap.get("id");

                //userInfo에 담기
                userInfo.put("nickname", nickname);
                userInfo.put("email", email);
                userInfo.put("profile_image",img);
                userInfo.put("id",id);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return userInfo;
    }


    @Override
    public List<Users> getAllUsers(Pager pager) {
        log.info("유저 리스트 호출!");
        return usersRepository.getAllUsers(pager);
    }

    @Override
    public Long getTotalUsers(Pager pager) {
        log.info("유저 총합계 호출!");
        return usersRepository.getTotalUsers(pager);
    }

    @Override
    public String getuserImg(String userName) {
        return usersRepository.getuserImg(userName);
    }

    @Override
    public String getUserName(String userName) {
        return usersRepository.getUserName(userName);
    }

    @Override
    public Long getUserId(String userName) {
        return usersRepository.getUserId(userName);
    }

    @Override
    public String getUserNickName(Long userId) {
        return usersRepository.getUserNickName(userId);
    }

    @Override
    public String getUserIdofuserImg(Long userId) {
        return usersRepository.getUserIdofuserImg(userId);
    }

    @Override
    public List<Users> getUsersByNewest() {
        return usersRepository.getUsersByNewest();
    }

    @Override
    public void changeAccess(Long userId) {
        Users byId = findById(userId);
        String role_access = null;

        //user일 경우 admin으로
        if (byId.getRole_access() == Role.USER) {
            byId.setRole_access(Role.ADMIN);
            role_access = Role.ADMIN.toString();
            log.info("change access : {}", byId.getRole_access());

        }
        //admin일 경우 user로
        else if (byId.getRole_access() == Role.ADMIN) {
            byId.setRole_access(Role.USER);
            log.info("change access : {}", byId.getRole_access());
            role_access = Role.USER.toString();
        } else log.info("에러발생!");

        usersRepository.changeAccess(userId, role_access);
    }

    @Override
    public void changeStore(Long userId) {
        Users byId = findById(userId);
        String role_store = null;

        // MEMBER => STOREMANAGER
        if (byId.getRole_store() == Role.MEMBER) {
            byId.setRole_store(Role.STOREMANAGER);
            role_store = Role.STOREMANAGER.toString();
            log.info("change store : {}", byId.getRole_store());

        }
        // STOREMANAGER => MEMBER
        else if (byId.getRole_store() == Role.STOREMANAGER) {
            byId.setRole_store(Role.MEMBER);
            log.info("change store : {}", byId.getRole_store());
            role_store = Role.MEMBER.toString();
        } else log.info("에러발생!");

        usersRepository.changeStore(userId, role_store);
    }

    @Override
    public void changeOnline(Long userId) {
        Users byId = findById(userId);
        String role_online = null;

        //ACTIVE => DISABLED
        if (byId.getRole_online() == Role.ACTIVE) {
            byId.setRole_online(Role.DISABLED);
            role_online = Role.DISABLED.toString();
            log.info("change store : {}", byId.getRole_online());

        }
        // DISABLED => ACTIVE
        else if (byId.getRole_online() == Role.DISABLED) {
            byId.setRole_online(Role.ACTIVE);
            log.info("change store : {}", byId.getRole_online());
            role_online = Role.ACTIVE.toString();
        } else log.info("에러발생!");

        usersRepository.changeOnline(userId, role_online);


    }

    @Override
    public void changeRead(Long userId) {
        Users byId = findById(userId);
        String role_read = null;

        //NONSUB => SUBSCRIBE
        if (byId.getRole_read() == Role.NONSUB) {
            byId.setRole_read(Role.SUB);
            role_read = Role.SUB.toString();
            log.info("change store : {}", byId.getRole_read());

        }
        //SUBSCRIBE +> NONSUB
        else if (byId.getRole_read() == Role.SUB) {
            byId.setRole_read(Role.NONSUB);
            log.info("change store : {}", byId.getRole_read());
            role_read = Role.NONSUB.toString();
        } else log.info("에러발생!");


        usersRepository.changeRead(userId, role_read);
    }

    @Override
    public List<StoreRequestVO> getMyStoreRequest(Long userId) {
        return usersRepository.getMyStoreRequest(userId);
    }



    //스토어매니저 요청 정보 가져오기
    @Override
    public List<StoreRequestVO> AllRequest(Long userId) {
        return usersRepository.getMyStoreRequest(userId);
    }

    @Override
    public List<StoreRequestVO> getmyReq(Long userId) {
        return usersRepository.getmyReq(userId);
    }

    @Override
    public List<StoreReject> getReject(Long userId) {
        return usersRepository.getReject(userId);
    }
}


