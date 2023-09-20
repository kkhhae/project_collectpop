package com.collectpop.controller;

import com.collectpop.domain.*;
import com.collectpop.security.CustomUser;
import com.collectpop.service.BannerService;
import com.collectpop.service.PopupStoreService;
import com.collectpop.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/collectpop")
@Controller
@RequiredArgsConstructor
//@SessionAttributes("userId")
@Slf4j
public class HomeController {

    private final PopupStoreService popupStoreService;
    private final BannerService bannerService;
    private final UsersService usersService;

    @Value("${file.dir}") // application.properties 에 저장한 파일 저장 경로 가져오기
    private String uploadPath;

    @GetMapping("")
    public String home(@AuthenticationPrincipal CustomUser customUser, Model model,
                       @ModelAttribute Users users, HttpSession session){

        List<Banner> bannerList = bannerService.getBanner();
        model.addAttribute("bannerList", bannerList);
        log.info("bannerList : {}", bannerList);

        List<PopupStoreVO> popupStoreList = popupStoreService.getMap();
        model.addAttribute("popupStoreList", popupStoreList);
        log.info("popupStoreList : " + popupStoreList);
        // 팝업스토어 해시태그 가져오기
        List<PopupStoreHashtagVO> popupStoreHashtagList = popupStoreService.getHash();
        model.addAttribute("popupStoreHashtagList", popupStoreHashtagList);
        log.info("popupStoreHashtagList : " + popupStoreHashtagList);
        // 팝업스토어 최신 등록순
        List<PopupStoreVO> getStoresSortedByNewest = popupStoreService.getStoresSortedByNewest();
        model.addAttribute("NewStore",getStoresSortedByNewest);
        // 팝업스토어 좋아요 많은순
        List<PopupStoreVO> storesSortedByLikes = popupStoreService.getStoresSortedByLikes();
        model.addAttribute("storeLike",storesSortedByLikes);
        // 팝업스토어 조회수 많은순
        List<PopupStoreVO> storesSortedByViews = popupStoreService.getStoresSortedByViews();
        model.addAttribute("storeViews", storesSortedByViews);

        // 모달 권한 부여
        if (customUser != null) {
            if (customUser.getUsers().getRole_read() != Role.SUB) {
                model.addAttribute("showModal", true);
            } else {
                model.addAttribute("showModal", false);
            }
        } else {
            model.addAttribute("showModal", false); // 로그인하지 않은 경우에도 모달을 표시하지 않음
        }
        if(customUser != null) {
            if (customUser.getUsers().getRole_read() == Role.SUB) {
                model.addAttribute("showModal", true);
            } else {
                model.addAttribute("showModal", false);
            }
        }
        List<String> imageUrls = new ArrayList<>();
        for (PopupStoreVO store : popupStoreList) {
            String imageUrl = "/collectpop/thumbnail/" + store.getThumbnail();  // 이미지 URL 생성
            imageUrls.add(imageUrl);
        }
       log.info("home customUser : {}", customUser);

        //로그인 안했을 때
        if(customUser == null){
            return "main";
        }

        //유저정보 보내주기
        users = customUser.getUsers();
        model.addAttribute("users", users); // 모델에 사용자 정보 추가
        log.info("users 객체가 가진 정보 : {}", users);

        //사용자 세션값 보내주기
        //session.setAttribute("users", users);
        if(users.getRole_online() == Role.DISABLED){
            log.info("이 계정은 비활성화 계정입니다.");

            return "users/checkSelf";
        }
      return "main";
    }
    @ResponseBody
    @GetMapping("/banner/{fileName}")
    public Resource getBanner(@PathVariable String fileName) throws  MalformedURLException {
        String filePath = "file:" + uploadPath + "banner/" + fileName;
        return new UrlResource(filePath);
    }

 // 스토어 추가사진 html 화면에 이미지 띄워주기 위한 경로 요청
    @ResponseBody
    @GetMapping("/additional/{fileName}") // <img th:scr... /additional/${fileName}
    public Resource getAddImages(@PathVariable String fileName) throws MalformedURLException {
        log.info("fileName : {}", fileName);
        String filePath = "file:" + uploadPath + "additional/" + fileName;
        log.info("filePath : {}", filePath);
        return new UrlResource(filePath);
    }



}
