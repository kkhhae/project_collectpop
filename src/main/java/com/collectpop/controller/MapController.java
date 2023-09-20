package com.collectpop.controller;

import com.collectpop.domain.PopupStoreVO;
import com.collectpop.domain.Users;
import com.collectpop.security.CustomUser;
import com.collectpop.service.PopupStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/collectpop")
@Controller
@RequiredArgsConstructor
@Slf4j
public class MapController {
    @Autowired
    private  PopupStoreService popupStoreService;

    @Value("${file.dir}") // application.properties 에 저장한 파일 저장 경로 가져오기
    private String uploadPath;

    @GetMapping("/map")
    public String MapForm (Model model, @AuthenticationPrincipal CustomUser customUser) {
        if(customUser != null) {
            log.info("유저 값: {}", customUser);
            model.addAttribute("users", customUser.getUsers());
        }else {
            log.info("세션에 사용자 정보가 없습니다.");
        }
        List<PopupStoreVO> popupStoreList = popupStoreService.getMap();
        model.addAttribute("popupStoreList", popupStoreList);
        // 이미지 URL 리스트 생성 및 Model에 추가
        List<String> imageUrls = new ArrayList<>();
        for (PopupStoreVO store : popupStoreList) {
            String imageUrl = "/collectpop/thumbnail/" + store.getThumbnail();  // 이미지 URL 생성
            imageUrls.add(imageUrl);
        }
        model.addAttribute("imageUrls", imageUrls);  // 이미지 URL 목록 전달
        log.info("popStoreList : " + popupStoreList);
        return "/map/map";
    }
    @ResponseBody
    @GetMapping("/mapData")
    public List<PopupStoreVO> getMapData() {
        List<PopupStoreVO> popupStoreList = popupStoreService.getMap();

        return popupStoreList;
    }

    // 스토어 대표사진 html 화면에 이미지 띄워주기 위한 경로 요청
    @ResponseBody
    @GetMapping("/thumbnail/{fileName}") // 웹상 요청 경로(img 태그의 src)
    public Resource getThumbImages(@PathVariable String fileName) throws MalformedURLException {

        log.info("fileName : {}", fileName);
        String filePath = "file:" + uploadPath + "thumbnail/" + fileName; // 실제 저장 폴더 위치
        log.info("filePath : {}", filePath);
        return new UrlResource(filePath);
    }
}
