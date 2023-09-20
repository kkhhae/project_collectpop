package com.collectpop.controller;

import com.collectpop.domain.Banner;
import com.collectpop.dto.DeleteBannerRequest;
import com.collectpop.service.BannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/collectpop/admin/banner")
public class BannerController {

    private final BannerService bannerService;

    @Value("${file.dir}") // application.properties에 저장한 파일 저장 경로 가져오기
    private String uploadPath;

    @GetMapping
    public String bannerPage() {
        // 여기에서 관리자 페이지에 대한 로직을 추가하세요.
        return "/admin/banner"; // 관리자 페이지의 뷰 이름을 반환합니다.
    }
    @GetMapping("/list")
    public ResponseEntity<List<Banner>> listBanners(Model model) {
        List<Banner> bannerList = bannerService.getBanner();
        model.addAttribute("bannerList", bannerList);
        return new ResponseEntity<>(bannerList, HttpStatus.OK);
    }

    @PostMapping("/deleteBanner")
    public ResponseEntity<String> deleteBanner(Long bnId) {
        log.info("deleteBanner 호출 - bnId: " + bnId); // 로그 추가
        bannerService.deleteBanner(bnId);
        return new ResponseEntity<>("deleteSuccess", HttpStatus.OK);
    }

    @PostMapping("/upload")
    public String uploadBanner(@RequestParam("files") List<MultipartFile> files, RedirectAttributes redirectAttributes, Model model) {
        if (files.isEmpty()) {
            // 파일이 업로드되지 않은 경우 처리
            redirectAttributes.addFlashAttribute("message", "파일을 선택해주세요.");
            return "redirect:/error";
        }

        try {
            int uploadedFileCount = 0; // 업로드된 파일 수를 저장할 변수

            for (MultipartFile file : files) { // 변수명을 'file'로 수정
                // UUID를 사용하여 고유한 파일 이름 생성
                String originalFileName = file.getOriginalFilename();
                String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String fileName = UUID.randomUUID().toString() + extension;
                String filePath = Paths.get(uploadPath, "banner", fileName).toString(); // 파일 경로 생성

                Banner banner = new Banner();
                banner.setFile_name(fileName); // UUID 값으로 파일 이름 저장
                banner.setFile_path(filePath);

                bannerService.insertBanner(banner);
                // Banner 객체를 이용하여 데이터베이스에 저장하는 로직을 추가하세요.
                // 예: bannerService.saveBanner(banner);

                // 파일을 실제로 업로드
                file.transferTo(new File(filePath));

                uploadedFileCount++; // 파일이 업로드되면 카운트 증가
            }

            // 업로드된 파일 수를 로그에 출력
            log.info(uploadedFileCount + "개의 파일이 업로드되었습니다.");

            return "redirect:/collectpop/admin/mainm/";
        } catch (IOException e) {
            // 파일 업로드 실패 처리
            redirectAttributes.addFlashAttribute("message", "파일 업로드 중 오류가 발생했습니다.");
            return "redirect:/error";
        }
    }

    @ResponseBody
    @GetMapping("/banner/{fileName}")
    public Resource getBanner(@PathVariable String fileName) throws  MalformedURLException {
        String filePath = "file:" + uploadPath + "banner/" + fileName;
        return new UrlResource(filePath);
    }
}
