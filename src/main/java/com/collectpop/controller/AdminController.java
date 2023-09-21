package com.collectpop.controller;


import com.collectpop.domain.*;
import com.collectpop.dto.PageDTO;
import com.collectpop.dto.Pager;
import com.collectpop.security.CustomUser;
import com.collectpop.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RequestMapping("/collectpop/admin/*")
@Controller
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private PopupStoreService popupStoreService;

    @Autowired
    private UsersService usersService;

    private final AdminService adminService;

    private final FeedService feedService;

    @Value("${file.dir}") // application.properties 에 저장한 파일 저장 경로 가져오기
    private String uploadPath;


    // 등업 신청 관리 리스트 페이지 요청
    @GetMapping("requestList")
    public String viewStoreRequests(Model model, @AuthenticationPrincipal CustomUser user) {
        List<StoreRequestVO> storeRequests = popupStoreService.getAllStoreRequests();

        Users users = user.getUsers();
        model.addAttribute("users", users);

        // 각 StoreRequestVO 객체에 이메일 값을 추가로 세팅
        for (StoreRequestVO storeRequest : storeRequests) {
            String userEmail = popupStoreService.getUserEmailById(storeRequest.getUserId());
            storeRequest.setEmail(userEmail);
        }


        model.addAttribute("storeRequests", storeRequests);
        return "admin/requestList";
    }

    //스토어매니저로 등업 신청 승인 처리
    @GetMapping("approveRequest/{requestId}")
    public String requestApprove(@PathVariable Long requestId, StoreRequestVO storeRequestVO, Model model){

        //requestId 넘어온거로 스토어매니저신청 정보 찾기
        StoreRequestVO requestById = popupStoreService.getRequestById(requestId);
        log.info("store request : {}", requestById);
        //또 그거로 userId 갖고오기
        Long userId = requestById.getUserId();
        log.info("to be storemanager : {}", userId);
        //한 다음 그 id로 유저정보 찾고 업데이트까지 승인처리
        Users byId = usersService.findById(userId);
        adminService.approveRequest(byId);
        log.info("스토어  유저 : {}",byId);
        // 승인 상태 설정 (예: 1은 승인)

        requestById.setStatus(1L);
        adminService.updateRequestStatus(requestById);


        return "redirect:/collectpop/admin/requestList";
    }
    //스토어매니저로 등업 신청 거부 처리
    @GetMapping("rejectRequest/{requestId}")
    public String requestReject(@PathVariable Long requestId, Model model, @AuthenticationPrincipal CustomUser customUser,
                                PopupStoreVO popupStoreVO){

        Users users = customUser.getUsers();
        model.addAttribute("users", users);

        StoreReject storeReject = new StoreReject();
        storeReject.setRequestId(requestId);

        StoreRequestVO requestById = popupStoreService.getRequestById(requestId);
        Long userId = requestById.getUserId();

        storeReject.setUserId(userId);

        log.info("store reject : {}", storeReject);
        model.addAttribute("reject", storeReject);

        StoreRequestVO storeRequests = popupStoreService.getRequestById(requestId);
        model.addAttribute("storeRequests", storeRequests);

        return "admin/rejectRequest";
    }
    @PostMapping("rejectRequest/{requestId}")
    public String rejectStorePro(@PathVariable Long requestId,
                                 @AuthenticationPrincipal CustomUser customUser,
                                 @ModelAttribute StoreReject reject){

        StoreRequestVO storeRequestVO = popupStoreService.getRequestById(requestId);
        reject.setUserId(storeRequestVO.getUserId());
        reject.setRequestId(storeRequestVO.getRequestId());
        reject.setStatus(2L);
        // 반려 상태 설정 (예: 2는 반려)
        storeRequestVO.setStatus(2L);
        adminService.updateRequestStatus(storeRequestVO);


        log.info("reject : {}", reject);
        adminService.rejectRequest(reject);

        return "redirect:/collectpop/admin/requestList";
    }



    @GetMapping("filter")
    public String sortStoreRequests(@RequestParam("status") String status, Model model) {
        List<StoreRequestVO> storeRequests = new ArrayList<>();

        log.info("status 값: {}", status);

        if (status.equals("newest")) {
            storeRequests = popupStoreService.getAllStoreRequestsSortedByNewest();
        } else if (status.equals("oldest")) {
            storeRequests = popupStoreService.getAllStoreRequestsSortedByOldest();
        }

        // 각 StoreRequestVO 객체에 이메일 값을 추가로 세팅
        for (StoreRequestVO storeRequest : storeRequests) {
            String userEmail = popupStoreService.getUserEmailById(storeRequest.getUserId());
            storeRequest.setEmail(userEmail);
        }

        model.addAttribute("storeRequests", storeRequests);

        log.info("정렬된 값 넘어오는지 확인: {}", storeRequests);

        return "admin/requestList :: #request-list"; // 특정 부분만 업데이트
    }



    // 등업신청 상세내역 페이지 요청
    // 신청 상세내역 페이지로 이동
    @GetMapping("/requestDetail/{requestId}")
    public String showRequestDetail(@PathVariable Long requestId, Model model,
                                    @AuthenticationPrincipal CustomUser user) {
        // requestId를 사용하여 db에서 해당 신청 내역 조회
        log.info("등업 고유번호: {}", requestId);

        Users users = user.getUsers();
        model.addAttribute("users", users);
        StoreRequestVO storeRequest = popupStoreService.getRequestById(requestId);

        model.addAttribute("storeRequest", storeRequest);

        log.info("등업 신청 정보: {}", storeRequest);

        return "admin/requestDetail"; // 뷰 이름
    }


    // 등업신청시 첨부한 첨부파일 다운로드 하기 위한 경로 요청
    @GetMapping(value = "/download/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws MalformedURLException {
        log.info("fileName : {}", fileName);
        String filePath = uploadPath + "documents/" + fileName;
        log.info("filePath : {}", filePath);

        Resource resource = new UrlResource("file:" + filePath);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename());

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
    @GetMapping("/mainm")
    public String mainmform(Model model){
        // 최근 올라온 팝업 스토어 리스트
        List<PopupStoreVO> storesSortedByNewest = popupStoreService.getStoresSortedByNewest();
        model.addAttribute("storesSortedByNewest",storesSortedByNewest);

        // 최근 올라온 피드
        List<FeedImg> getFeedByNewest = feedService.getFeedImg();
        model.addAttribute("getFeedByNewest", getFeedByNewest);

        // 최근 가입한 유저
        List<Users> getUsersByNewest = usersService.getUsersByNewest();
        model.addAttribute("getUsersByNewest",getUsersByNewest);
        // 최근 등업 신청
        List<StoreRequestVO> getAllStoreRequestsSortedByNewest = popupStoreService.getAllStoreRequestsSortedByNewest();
        model.addAttribute("getAllStoreRequestsSortedByNewest", getAllStoreRequestsSortedByNewest);


        return "admin/mainm";
    }
    @GetMapping("/declaration")
    public String declarationform(Pager pager, Model model){
        log.info("pager = {}" , pager);
        model.addAttribute("decList", adminService.getdecListWithPaging(pager));
        model.addAttribute("pageDTO", new PageDTO(pager, adminService.getTotaldec(pager)));
        return "admin/declaration";
    }
    @GetMapping("/decexmanaged")
    public String decexmanagedform(){
        return "admin/decexmanaged";
    }
    @PostMapping("/insertEx")
    public ResponseEntity<String> insertEx(String text){
        log.info("insertEx String text = {}", text);
        adminService.insertEx(text);
        return new ResponseEntity<>("저장 되었습니다.", HttpStatus.OK);
    }
    @GetMapping("printhtml")
    public ResponseEntity<List<DecExample>> print(){
        List<DecExample> result = adminService.getAllDecEx();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PostMapping("deletedecex")
    public ResponseEntity<String> deleteDecEx(Long dleid){
        log.info("deleteDecEx dleid = {}", dleid);
        adminService.deleteExample(dleid);
        return new ResponseEntity<>("삭제 되었습니다.", HttpStatus.OK);
    }
    @PostMapping("/adddec")
    public ResponseEntity<String> adddec(Declaration declaration){
        log.info("declaration = {}", declaration);
        adminService.addDeclaration(declaration);
        return new ResponseEntity<>("신고 접수되었습니다", HttpStatus.OK);
    }
    //회원 목록 관리
    @GetMapping("/userList")
    public String usersAdmin(Pager pager , Model model, @AuthenticationPrincipal CustomUser customUser){
        //시큐리티 세션값
        model.addAttribute("users", customUser.getUsers());

        model.addAttribute("userList", usersService.getAllUsers(pager));
        model.addAttribute("pageDTO", new PageDTO(pager, usersService.getTotalUsers(pager)));
        return "admin/userList";
    }
    //접속권한 : 사용자/관리자
    @PostMapping("/accessChange")
    public ResponseEntity<String> accessChange(@RequestParam Long userId) {
        try {
            usersService.changeAccess(userId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed: " + e.getMessage());
        }
    }
    //접속권한 : 회원/스토어매니저
    @PostMapping("/storeChange")
    public ResponseEntity<String> storeChange(@RequestParam Long userId) {
        try {
            usersService.changeStore(userId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed: " + e.getMessage());
        }
    }
    //접속권한 : 온라인/비활성화
    @PostMapping("/onlineChange")
    public ResponseEntity<String> onlineChange(@RequestParam Long userId) {
        try {
            usersService.changeOnline(userId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed: " + e.getMessage());
        }
    }
    //접속권한 : 비구독/구독
    @PostMapping("/readChange")
    public ResponseEntity<String> readChange(@RequestParam Long userId) {
        try {
            usersService.changeRead(userId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed: " + e.getMessage());
        }
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
    @ResponseBody
    @GetMapping("/feedimg/{fileName}") // 웹상 요청 경로(img 태그의 src)
    public Resource getFeeimg(@PathVariable String fileName) throws MalformedURLException {

        log.info("fileName : {}", fileName);
        String filePath = "file:" + uploadPath + fileName; // 실제 저장 폴더 위치
        log.info("filePath : {}", filePath);
        return new UrlResource(filePath);
    }





}
