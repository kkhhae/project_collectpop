package com.collectpop.controller;

import com.collectpop.domain.*;
import com.collectpop.repository.UsersMapper;
import com.collectpop.repository.UsersRepository;
import com.collectpop.security.CustomUser;
import com.collectpop.service.ImgUpload;
import com.collectpop.service.PopupStoreService;
import com.collectpop.service.UserRandomService;
import com.collectpop.service.UsersService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/collectpop/users/")
@Controller
@SessionAttributes("userId")
public class UsersController {


    //테스트ㅡㅡㅡ

    private final PasswordEncoder passwordEncoder;

    private final UsersService usersService;
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
    private final UserRandomService userRandomService;
    private final ImgUpload imgUpload;
    private final PopupStoreService popupStoreService;
    // application.properties 에 저장한 파일 저장 경로 가져오기
    //경로 기본 설정 : 실제 패키지 내 userImg에 저장되도록 해두었습니다. 세팅 시 참고해주세요
    @Value("${file.dir}")
    private String uploadPath;

    // 프로필사진 파일 생성
//    private String saveImg(MultipartFile profileImg) throws Exception{
//
//        if (profileImg == null || profileImg.isEmpty() || profileImg.getOriginalFilename() == null) {
//            //기본이미지설정
//            return "/assets/userImg/profile.png";
//        }
//        String uuid = UUID.randomUUID().toString();
//        String extension;
//        extension = profileImg.getOriginalFilename().substring(profileImg.getOriginalFilename().lastIndexOf(".")); // 파일 확장자명 가져오기
//
//        String fileName = uuid + "_" + extension;
//
//        File destFile = new File(uploadPath + "userImg/" + fileName);
//        log.info("savefile con : {} ",destFile);
//        profileImg.transferTo(destFile);
//
//        return fileName;
//    }


    // 대표사진 파일 생성
    private String saveImg(MultipartFile imageFile) throws IOException {
        if (imageFile == null || imageFile.isEmpty() || imageFile.getOriginalFilename() == null) {
            //기본이미지설정
            return "assets/profile.png";
        }
        String uuid = UUID.randomUUID().toString();
        String extension = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf(".")); // 파일 확장자명 가져오기
        String fileName = uuid + "_" + extension;
        File destFile = new File(uploadPath + fileName); // F:\\ljm\\springboot\\upload\\thumbnail 에 저장
        imageFile.transferTo(destFile);
        return fileName;
    }


    //user 마이페이지
    @GetMapping("/mypage/{userId}")
    public String mypage(@PathVariable("userId") Long userId, Model model){
        Users user = usersService.findById(userId);
        model.addAttribute("users", user);

        log.info("mypageUser:{}",user);
        return "users/mypage";
    }

    @ResponseBody
    @GetMapping("{fileName}")
    public Resource getProfile(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" + uploadPath +"userImg/"+ fileName);
    }
    // 내가 올린 팝업스토어
    @GetMapping("/myStore")
    public String myStoreView (Model model, @AuthenticationPrincipal CustomUser customUser) {
        Users users = customUser.getUsers();
        model.addAttribute("users", users);
        List<PopupStoreVO> getPopupStoreByUesrId = popupStoreService.getPopupStoreByUesrId();
        model.addAttribute("myStore",getPopupStoreByUesrId);

        return "/users/myStore";
    }



    //회원정보 수정페이지
    @GetMapping("/edit/{userId}")
    public String editUser(@PathVariable("userId") Long userId, Model model, HttpServletResponse response) throws IOException{

    //pk값으로 유저 찾기
        Users editbyId = usersService.findById(userId);

        //카카오유저 수정못하게
        if (editbyId.getAddress().equals("[KAKAO_ACOUNT_NULL]")) {
            log.info("카카오유저입니다.");
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('카카오유저는 수정할 수 없습니다!');history.go(-1);</script>");
            out.flush();
        }

        editbyId.setImg("/asset/profile.png");
        model.addAttribute("users", editbyId);
        log.info("getready update user:{}",editbyId);

        return "/users/edit";
    }
    @PostMapping("/edit/{userId}")
    public String editPro(@PathVariable("userId") Long userId, @ModelAttribute Users users,
                          MultipartFile profileImg, Model model) throws Exception {

        log.info("join imageFile : {}",profileImg );
        //pk값으로 유저 찾기
        Users findUser = usersService.findById(userId);
        String inputPass = findUser.getPassword();

        // 원래 비밀번호와 저장된 암호화된 비밀번호 비교
        if (passwordEncoder.matches(users.getPassword(), inputPass)) {

            // 프로필사진 업로드 처리
            String imgPath = saveImg(profileImg);
//            users.setImg(uploadPath +"userImg/" + imgPath);
            users.setImg(imgPath);
            // 비번 맞으면 수정
            usersService.update(users);
            log.info("update after user : {} ", users);
//            usersService.updateImg(users.getImg(), userId);

            model.addAttribute("users", users);
            // 상세 페이지로 이동
            return "redirect:/collectpop/users/mypage/" + users.getUserId();
        } else {
            log.info("fail update..");
            return "redirect:/collectpop/error";
//            return "redirect:/collectpop/users/" + users.getUserId() + "/edit";
        }
    }


    //회원가입 페이지
    @GetMapping("/join")
    public String signup(@ModelAttribute Users users){

        return "/users/join";
    }
    //회원가입 처리
    @PostMapping("/join")
    public String signupPro(MultipartFile profileImg,
                            @ModelAttribute Users users, Model model
    ) throws IOException {
        log.info("join imageFile : {}",profileImg );
        try{
            // 프로필사진 업로드 처리
            String imgPath = saveImg(profileImg);
//            log.info("imgPath : {}", imgPath);
//            users.setImg(imgPath);
//            String upload = imgPath;
            String upload = imgPath;
            log.info("upload  : {}", upload);

            // 대표사진 썸네일 이미지 경로 전달
            model.addAttribute("img", upload);
            // db에 경로 저장
            users.setImg(upload);
            log.info("user: {} ", users);

            //사용자의 닉네임이 공백 또는 null일 시 랜덤닉네임 지정
            String code = userRandomService.createName();
            log.info("랜덤 닉네임 지정! " + code);
            if("".equals(users.getNickname()) || users.getNickname() == null){
                users.setNickname(code);
                log.info("join after random user : {}", users);
            }

            log.info("회원가입 성공한 유저 : {}", users);
            usersService.signupImg(users);

        }catch (Exception e){
            e.printStackTrace();

            return "redirect:/collectpop/error";
        }

        return "redirect:/collectpop/users/completeJoin";
    }

    @GetMapping("/completeJoin")
    public String joinPro(){
        return "users/completeJoin";
    }


    // 로그인 폼 (로그인처리는 시큐리티가)
    @GetMapping("/login")
    public String loginForm(@ModelAttribute Users users) {

        if(users.getRole_online()==Role.DISABLED){
            log.info("비활성화 계정 본인확인 페이지 호출");
            return "/users/checkSelf";
        }

        return "/users/login";
    }
    //로그아웃 : 시큐리티로 처리




    //아이디 찾기 페이지 (이메일 인증)
    @GetMapping("/findUsername")
    public String findId (@ModelAttribute Users users){
        return "/users/findUsername";
    }
    @PostMapping("/findUsername")
    public String findIdPro(Model model, @ModelAttribute Users users,
                            @RequestParam("email") String email) {
        Users findEmail = usersService.findbyIdWithEmail(email);
        log.info("find email DB : {}", findEmail);

        if (findEmail != null && findEmail.getEmail().equals(email)) {
            //이메일 인증하고 찾아오기, 이메일 중복가입 불가하면 따로 비교처리 안해도될듯?싶습니다.
            log.info("찾기성공");
            model.addAttribute("users", findEmail);

            return "users/findUserResult";
        }
        log.info("아이디 찾기실패..");
        return "redirect:/collectpop/error";

    }


    //비밀번호 찾기 페이지
    @GetMapping("/findPassword")
    public String findPw (@ModelAttribute Users users){
        return "/users/findPassword";
    }
    //비밀번호 찾기 처리  + 재설정
    @PostMapping("/findPassword")
    public String findPwPro(@ModelAttribute Users users, Model model
            , @RequestParam("username") String username
            , @RequestParam("email") String email
            , @RequestParam("password")String password ){

        //이메일, 유저이름(id)받아와서 있나 검색
        log.info("user write username : {}", username);
        log.info("user write email : {}",email);

        Users find = usersService.findEmailandName(username,email);

        if (find.getUsername().equals(username) && find.getEmail().equals(email)) {

            //비밀번호 재설정
            find.setPassword(password);
            usersService.updatePw(find);
            log.info("update User : {}",find);
            log.info("업데이트 완료");
            return "/users/passPro";
        }
        log.info("비밀번호 찾기실패..");
        return "redirect:/collectpop/error";
    }
    //비밀번호 찾기 이후 처리
    @GetMapping("/passPro")
    public String passPro(){
        return "users/Passpro";
    }


    //회원 비활성화 처리
    @PostMapping("/users/offline")
    public String delete(@ModelAttribute Users users){
        usersService.offline(users);
        log.info("계정 비활성화 완료");
        return "redirect:/collectpop/";
    }


    //카카오 로그인 및 회원가입 처리 (시큐리티 사용)
    @GetMapping("/kakaoLogin")
    public String kakaoLogin(@RequestParam(value = "code", required = false) String code,
                             HttpSession session, MultipartFile multipartFile) throws Throwable {
        //요청 시 넘어온 코드값
        log.info("controller code:" + code);
        //코드값으로 토큰얻기
        String access_Token = usersService.getAccessToken(code);
        log.info("controller Token:" + access_Token); //정상작동

        //토큰값으로 유저정보 받아오기
        HashMap<String, Object> userInfo = usersService.getUserInfo(access_Token);
        log.info("kakaoUser : {}", userInfo);
        //userInfo에서 해당 계정 id값 받기
        Long kakaoId = (Long) userInfo.get("id");
        log.info("kakaoId : {}", kakaoId);
        //기존 유저가 있는지 확인
        Users userFind = usersMapper.findByUsername(userInfo.get("id").toString());
        log.info("user Find and : {} ", userFind);

        //기존 유저의 정보와 일치하지 않을 시 생성
        if (userFind == null || !userFind.getUsername().equals(kakaoId.toString())) {
            Users newUser = new Users();

            // 새로운 사용자라면 데이터베이스에 저장
            newUser.setUsername(kakaoId.toString());

            newUser.setEmail((String) userInfo.get("email"));
            //이메일 서비스 미동의 시 null값 처리,
            if(userInfo.get("email") == null){
                newUser.setEmail("NO EMAIL");
            }

            newUser.setNickname((String) userInfo.get("nickname"));

            //프로필사진 업로드 처리
            newUser.setImg(userInfo.get("profile_image").toString());

            //카카오에서 제공하지 않는 정보들 따로 세팅
            newUser.setPassword("[KAKAO_ACOUNT_NULL]");
            newUser.setAddress("[KAKAO_ACOUNT_NULL]");
            newUser.setPhoneNum("[KAKAO_ACOUNT_NULL]");

            //role추가
//            newUser.setRole_access(Role.USER);   //사용자모드로 가입 <-> 관리자
//            newUser.setRole_store(Role.MEMBER); //일반회원으로 가입 <-> 스토어매니저
//            newUser.setRole_read(Role.NONSUB); //비구독상태로 가입 <-> 구독
//            newUser.setRole_online(Role.ACTIVE); //온라인상태로 가입 <-> 비활성화

            log.info("new User : {}", newUser);
            usersService.signupImg(newUser);
            log.info("회원 가입 완료");

            return "redirect:/collectpop/users/completeJoin";
        }
        //이미 가입되어있을 경우
        else if (userFind.getUsername().equals(kakaoId.toString())) {
            log.info("이미 계정이 존재합니다! 로그인 진행");

            CustomUser customUser = new CustomUser(userFind);
            log.info("already user : {} ", customUser);

            //인증처리
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    customUser,
                    customUser.getPassword(),
                    customUser.getAuthorities()
            );

            //담고있는 보안정보를 시큐리티로 (인증된 사용자라는걸 알리는 설정 처리)
            SecurityContextHolder.getContext().setAuthentication(authentication);

            session.setAttribute("userId", customUser);

            return "redirect:/collectpop/";
        }
        else {
            return "redirect:/collectpop/error";
        }

    }


    //유저 비활성화
    @GetMapping("/offline/{userId}")
    public String offline(@PathVariable("userId") Long userId, @ModelAttribute Users users,Model model){
        //pk값으로 유저 찾기
        Users findUser = usersService.findById(userId);
        model.addAttribute("users", findUser);

        log.info("비활성화 신청!");

        return "users/offline";
    }
    //비활성화 처리
    @PostMapping("/offline/{userId}")
    public String offlinePro(@ModelAttribute Users users){
        log.info("비활성화 처리..");
        usersService.offline(users);
        log.info("비활성화 완료.");

        return "redirect:/logout";
    }
    //비활성화 계정 풀기 (사용자가 입력한 이메일값이랑 기존에 저장되어있던 유저의 이메일 값 비교)
    @PostMapping("/checkSelf")
    public String checkSelf(@ModelAttribute Users users, @RequestParam("email") String email){
        Users findUser = usersService.findbyIdWithEmail(users.getEmail());
        log.info("find email user : {}", findUser);

        if(findUser == null){
            log.info("이메일이 틀립니다.");
            return "redirect:/collectpop/error";

        }
        usersService.online(findUser);
        log.info("success email  " + email + " << >> " + findUser.getEmail());

        return "redirect:/collectpop/main";
    }

    // 등업 신청 관리 리스트 페이지 요청
    @GetMapping("/myList/{userId}")
    public String myList(Model model, @AuthenticationPrincipal CustomUser customUser,
                         @PathVariable Long userId) {

        Users users = customUser.getUsers();
        List<StoreReject> reject = usersService.getReject(userId);
        log.info("reject:{}", reject);
        for(StoreReject storeReject : reject){
            if(storeReject.getUserId() == users.getUserId()) {
                model.addAttribute("reject", storeReject.getContent());
            }
            log.info(storeReject.getContent());
        }

        List<StoreRequestVO> myStoreRequest = usersService.AllRequest(users.getUserId());
        for (StoreRequestVO storeRequest : myStoreRequest) {

            if(storeRequest.getStatus() == 1) {
                storeRequest.setSetStatusShow("승인");
            } else if (storeRequest.getStatus() == 2) {
                storeRequest.setSetStatusShow("반려됨");
            } else if (storeRequest.getStatus() == 0){
                storeRequest.setSetStatusShow("대기");
            }
            //로그 확인
            log.info("등업 신청 정보: {}", storeRequest);
        }
        model.addAttribute("storeRequests", myStoreRequest);

        //유저정보 담기
        model.addAttribute("users", users);

        //반려사유 들고오기
        List<StoreRequestVO> getmyReq = usersService.getmyReq(users.getUserId());
        log.info("확인용 내 리퀘스트 : {}", getmyReq);

        //스토어매니저가 승인됬을 경우 상단 표기
//        if(users.getRole_store() == Role.STOREMANAGER){
//            model.addAttribute("storeAccess", "승인됨");
//        }
//        //대기
//        else  model.addAttribute("storeAccess", "대기");

        return "/users/myList";
    }

    //아이디 중복검사용
    @PostMapping("/checkId")
    @ResponseBody // ajax로 값을 보내기위해 사용
    public String checkId(@RequestParam("username") String username) {

        //아이디 찾아와서 중복검사 ( 1= 중복, 0=중복x)
        boolean result = usersService.findUsername(username);

        //아이디가 없을시 true, 있으면 false 으로 보냄
        String str = "";
        if(result) { // —> 'null'==true
            str="true";
        }else if(!result){
            str="false";
        }
        return str;
    }

    //유저맵 가져오기( 1, 서울, 위도, 경도 등)
    @ModelAttribute("userMap")
    public List<UserMap> userMaps(){
        List<UserMap> getuserMap = usersService.getUserMap();
//        log.info("usermap : {}", getuserMap);
        return getuserMap;
    }

    @ResponseBody
    @GetMapping("/userImg/{img}")
    public Resource getUser(@PathVariable String img) throws  MalformedURLException {
        String ImgPath = "file:" + uploadPath + "userImg/" + img;
        return new UrlResource(ImgPath);
    }

}
