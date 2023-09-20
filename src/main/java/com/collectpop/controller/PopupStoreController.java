package com.collectpop.controller;

import com.collectpop.domain.*;
import com.collectpop.security.CustomUser;
import com.collectpop.service.PopupStoreService;
import com.collectpop.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequestMapping("/collectpop/store/*")
@Controller
@RequiredArgsConstructor
//@SessionAttributes("userId") 주석처리
public class PopupStoreController {

    @Autowired
    private PopupStoreService popupStoreService;

    @Autowired
    private UsersService usersService;

    @Value("${file.dir}") // application.properties 에 저장한 파일 저장 경로 가져오기
    private String uploadPath;


    // 대표사진 파일 생성
    private String saveThumbnail(MultipartFile imageFile) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String extension = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf(".")); // 파일 확장자명 가져오기
        String fileName = uuid + "_" + extension;

        File destFile = new File(uploadPath + "/thumbnail/" + fileName); // F:\\ljm\\springboot\\upload\\thumbnail 에 저장
        imageFile.transferTo(destFile);

        return fileName;
    }

    // 추가 이미지 파일 생성
    private String saveImage(MultipartFile imageFile) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String extension = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf(".")); // 파일 확장자명 가져오기
        String fileName = uuid + "_" + extension;

        File destFile = new File(uploadPath + "/additional/" + fileName); // F:\\ljm\\springboot\\upload\\additional 에 저장
        imageFile.transferTo(destFile);

        return fileName;
    }

    // 스토어 등업관련 첨부파일 생성
    private String saveDocuments(MultipartFile imageFile) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String extension = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf(".")); // 파일 확장자명 가져오기
        String fileName = uuid + "_" + extension;

        // 업로드할 파일 생성F
        File destFile = new File(uploadPath + "/documents/" + fileName); // F:\\ljm\\springboot\\upload\\documents 에 저장
        // 파일 업로드
        imageFile.transferTo(destFile);

        return fileName;
    }


    // 팝업스토어 등록페이지 요청
    @GetMapping("addStore")
    public String addStore(@AuthenticationPrincipal CustomUser customUser, Model model) {
        String thumbnailPath = "/path/to/thumbnail-image.jpg"; // 대표사진 썸네일 이미지 경로 설정
        model.addAttribute("thumbnailPath", thumbnailPath);

        if(customUser != null){
            log.info("유저 값: {}", customUser);
            model.addAttribute("users", customUser.getUsers());
        }else {
            log.info("세션에 사용자 정보가 없습니다.");
            // 사용자 정보가 세션에 없을 때 처리할 내용을 추가.
        }

        return "store/addStore";
    }



    // 팝업스토어 등록, 추가이미지 및 해시태그 저장처리
    @PostMapping("addStore")
    public String addStorePro(MultipartFile imageFile,MultipartFile[] additionalImageFiles,
                              @ModelAttribute PopupStoreVO popupStore,
                              @RequestParam("hashtagValues") String hashtagValues,
                              Model model, @AuthenticationPrincipal CustomUser customUser) {
        try {
            // 대표사진 업로드 처리
            String thumbnailPath = saveThumbnail(imageFile);
            // 업로드된 이미지 경로설정
            popupStore.setThumbnail(thumbnailPath);
            //popupStore.setUserId(customUser.getUsers().getUserId());
            // 대표사진 썸네일 이미지 경로 전달
            model.addAttribute("thumbnailPath", thumbnailPath);

            List<StorePhotosVO> additionalPhotos = new ArrayList<>();
            for (MultipartFile file : additionalImageFiles) {
                if (!file.isEmpty()) {
                    String imagePath = saveImage(file);
                    StorePhotosVO additionalPhoto = new StorePhotosVO();
                    additionalPhoto.setPhotoPath(imagePath);
                    additionalPhotos.add(additionalPhoto);
                }
            }
            model.addAttribute("additionalPhotos", additionalPhotos);

            // 로그 출력
            log.info("팝업스토어 정보: {}", popupStore);
            log.info("해시태그 입력: {}", hashtagValues);
            log.info("추가이미지 파일: {}", additionalPhotos);

            // 스토어 정보 및 추가이미지 해시태그 저장 요청
            popupStoreService.addStore(popupStore, additionalPhotos , hashtagValues);

        }catch (Exception e) {
            e.printStackTrace();
            // 파일 업로드 실패 처리
            // 실패 시에 처리할 내용을 여기에 작성
            return  "redirect:/error"; // 에러 페이지로 리다이렉트
        }
        return "redirect:/collectpop"; // 메인페이지로 이동
    }

    // 스토어 정보 수정 처리
    @PostMapping("updateStore")
    public String editStorePro(@ModelAttribute("updatedStore") PopupStoreVO updatedStore,
                               MultipartFile thumbnailImage,
                               MultipartFile[] additionalImages,
                               @RequestParam("hashtagValues") String hashtagValues,
                               Model model) {

        // 로그 출력
        log.info("가져온 수정할 정보: {}", updatedStore);
        log.info("가져온 해시태그 정보: {}", hashtagValues);
        log.info("가져온 이미지파일: {}", thumbnailImage);
        log.info("가져온 추가이미지파일: {}", additionalImages);
        try {
            // 이전 대표사진 삭제 및 새 대표사진 업로드 처리
            if (thumbnailImage != null && !thumbnailImage.isEmpty()) {
                // 이전 대표사진 파일 경로 가져오기
                String previousThumbnailPath = popupStoreService.getPreviousThumbnailPathByPsId(updatedStore.getPsId());
                log.info("기존사진 파일명 : {}", previousThumbnailPath);
                if (previousThumbnailPath != null && !previousThumbnailPath.isEmpty()) {
                    File previousThumbnailFile = new File(uploadPath + "thumbnail/" + previousThumbnailPath);
                    log.info("기존사진 저장 경로: {}", previousThumbnailFile.getAbsolutePath()); // 파일 경로 확인
                    if (previousThumbnailFile.exists()) {
                        boolean deleted = previousThumbnailFile.delete(); // 이전 대표사진 파일 삭제
                        log.info("이전 대표사진 삭제 여부: {}", deleted); // 파일 삭제 결과 확인
                    }
                }

                String thumbnailPath = saveThumbnail(thumbnailImage);
                // 업로드된 이미지 경로설정;
                updatedStore.setThumbnail(thumbnailPath);
                // 대표사진 썸네일 이미지 경로 전달
                model.addAttribute("thumbnailPath", thumbnailPath);
            }

            List<StorePhotosVO> additionalPhotos = new ArrayList<>();
            for (MultipartFile file : additionalImages) {
                if (file != null && !file.isEmpty()) {
                    String imagePath = saveImage(file);
                    StorePhotosVO additionalPhoto = new StorePhotosVO();
                    additionalPhoto.setPhotoPath(imagePath);
                    additionalPhotos.add(additionalPhoto);
                }
            }
            model.addAttribute("additionalPhotos", additionalPhotos);

            // 스토어 정보 및 추가이미지 해시태그 업데이트 요청
            popupStoreService.editStore(updatedStore, additionalPhotos, hashtagValues);

        } catch (Exception e) {
            e.printStackTrace();
            // 파일 업로드 실패 처리
            // 실패 시에 처리할 내용을 여기에 작성
            return "redirect:/error"; // 에러 페이지로 리다이렉트
        }
        return "redirect:/collectpop/store/storeDetail/" + updatedStore.getPsId();
    }


    // 팝업스토어 등업신청 페이지 요청
    @GetMapping("request")
    public String StoreRequest(@AuthenticationPrincipal CustomUser customUser, Model model) {
        //Users users = (Users) session.getAttribute("users");
        if(customUser != null){
            log.info("유저 값: {}", customUser);
            model.addAttribute("users", customUser.getUsers());
        }else {
            log.info("세션에 사용자 정보가 없습니다.");
            // 사용자 정보가 세션에 없을 때 처리할 내용을 추가.
        }
        return "store/request";
    }

    // 팝업스토어 등업신청 등록
    @PostMapping ("request")
    public String StoreRequestPro(MultipartFile documentFile1,
                                  MultipartFile documentFile2,
                                  MultipartFile documentFile3,
                                  @ModelAttribute StoreRequestVO storeRequest,
                                  @AuthenticationPrincipal CustomUser customUser,
                                  Model model) {
        try {
            // 증빙서류 파일 처리 로직
            if (!documentFile1.isEmpty()) {
                log.info("doc1 : {}", documentFile1.getOriginalFilename());
                log.info("doc1 : {}", documentFile1.getContentType());
                String document1Path = saveDocuments(documentFile1);
                storeRequest.setDocument1(document1Path); //저장한 파일 경로를 storeRequest 객체에 저장
            }
            if (!documentFile2.isEmpty()) {
                log.info("doc2 : {}", documentFile2.getOriginalFilename());
                log.info("doc2 : {}", documentFile2.getContentType());
                String document2Path = saveDocuments(documentFile2);
                storeRequest.setDocument2(document2Path);
            }
            if (!documentFile3.isEmpty()) {
                log.info("doc3 : {}", documentFile3.getOriginalFilename());
                log.info("doc3 : {}", documentFile3.getContentType());
                String document3Path = saveDocuments(documentFile3);
                storeRequest.setDocument3(document3Path);
            }


            //등업신청 시 회원 정보 userId, email 미리 세팅해서 보내주기 0906추가
            Users users = customUser.getUsers();
            storeRequest.setUserId(users.getUserId());
            storeRequest.setEmail(users.getEmail());
            // 로그 출력
            log.info("등업신청 정보: {}", storeRequest);

            model.addAttribute("users", users);

            popupStoreService.saveRequest(storeRequest);

            return "redirect:/collectpop"; // 메인페이지로 이동
        } catch (Exception e) {
            e.printStackTrace();
            // 파일 업로드 실패 처리
            return "redirect:/error"; // 에러 페이지로 리다이렉트
        }
    }


    // 스토어리스트 페이지 요청
    @GetMapping("storeList")
    public String StoreList(@AuthenticationPrincipal CustomUser customUser, Model model) {

        List<PopupStoreVO> popupStores = popupStoreService.getAllPopupStores(); // 전체 팝업 스토어 저장정보 가져오기
        // 팝업 스토어 객체 로그로 출력
        for (PopupStoreVO popupStore : popupStores) {
            log.info("팝업 스토어 해쉬 X : {}", popupStore);
        }

        for (PopupStoreVO popupStore : popupStores) {
            Long psId = popupStore.getPsId();
            List<String> hashtags = popupStoreService.getHashtagsByPsId(psId); // 해당 스토어의 해시태그 가져오기
            popupStore.setHashtags(hashtags); // 해시태그 정보 설정
            log.info("팝업 스토어 정보 해쉬 0 : {}", popupStore);
        }


        if(customUser != null) {
            log.info("유저 값: {}", customUser);
            model.addAttribute("users", customUser.getUsers());
        }else {
            log.info("세션에 사용자 정보가 없습니다.");
        }
        /*Users users = (Users) session.getAttribute("users");
        if (users != null) {
            log.info("세션 유저 값: {}", users);
            // 여기에서 사용자 정보를 활용할 수 있습니다.
            model.addAttribute("users", users); // 모델에 사용자 정보 추가
        } else {
            log.info("세션에 사용자 정보가 없습니다.");
            // 사용자 정보가 세션에 없을 때 처리할 내용을 추가.
        }*/

        model.addAttribute("popupStores", popupStores); // 모델에 팝업 스토어 정보 리스트 추가

        return "store/storeList";
    }


    // 전체,현재오픈,오픈예정,지난스토어 정렬 (1.정렬 유형)
    @GetMapping("filter1")
    public String filterStores1(@RequestParam("status1") String status1,
                                Model model) {
        List<PopupStoreVO> filteredStores = new ArrayList<>();

        // status1에 따라서 스토어를 가져오는 로직
        if (status1.equals("all")) {
            filteredStores = popupStoreService.getAllPopupStores();
        } else if (status1.equals("current")) {
            // 현재 오픈 중인 스토어 가져오기
            filteredStores = popupStoreService.getCurrentPopupStores();
        } else if (status1.equals("upcoming")) {
            // 오픈 예정인 스토어 가져오기
            filteredStores = popupStoreService.getUpcomingPopupStores();
        } else if (status1.equals("past")) {
            // 지난 스토어 가져오기
            filteredStores = popupStoreService.getPastPopupStores();
        }

        for (PopupStoreVO popupStore : filteredStores) {
            Long psId = popupStore.getPsId();
            List<String> hashtags = popupStoreService.getHashtagsByPsId(psId); // 해당 스토어의 해시태그 가져오기
            popupStore.setHashtags(hashtags); // 해시태그 정보 설정
            log.info("팝업 스토어 정보 해쉬 0 : {}", popupStore);
        }

        model.addAttribute("popupStores", filteredStores); // 모델에 필터링된 팝업 스토어 정보 리스트 추가
        return "store/storeList :: .store-card"; // 특정 부분만 업데이트
    }

    // 최신순, 오래된순, 좋아요 순, 조회순 (2.정렬 순서)
    @GetMapping("filter2")
    public String filterStores2(@RequestParam("status2") String status2,
                                Model model) {
        List<PopupStoreVO> filteredStores = new ArrayList<>();

        // status2에 따라서 스토어를 가져오는 로직
        if (status2.equals("newest")) {
            filteredStores = popupStoreService.getStoresSortedByNewest();
        } else if (status2.equals("oldest")) {
            filteredStores = popupStoreService.getStoresSortedByOldest();
        } else if (status2.equals("likes")) {
            filteredStores = popupStoreService.getStoresSortedByLikes();
        } else if (status2.equals("views")) {
            filteredStores = popupStoreService.getStoresSortedByViews();
        }

        for (PopupStoreVO popupStore : filteredStores) {
            Long psId = popupStore.getPsId();
            List<String> hashtags = popupStoreService.getHashtagsByPsId(psId); // 해당 스토어의 해시태그 가져오기
            popupStore.setHashtags(hashtags); // 해시태그 정보 설정
            log.info("팝업 스토어 정보 해쉬 0 : {}", popupStore);
        }

        model.addAttribute("popupStores", filteredStores); // 모델에 필터링된 팝업 스토어 정보 리스트 추가
        return "store/storeList :: .store-card"; // 특정 부분만 업데이트
    }

    // 3번 조합정렬
    @GetMapping("filter3")
    public String filterStores3(@RequestParam("status3") String status3,
                                Model model) {

        // status3 받아온 정보값 분리시킴
        String[] parts = status3.split("_");
        String status1 = parts[0]; // 현재 오픈, 오픈 예정, 지난 스토어
        String status2 = parts[1]; // 최신 등록 순, 오래된 순, 좋아요 순, 조회수 순

        // 팝업 서비스임플에서  분리된 정보값에 맞춰서 객체로 정렬된값 받아옴
        List<PopupStoreVO> filteredAndSortedStores = popupStoreService.getFilteredAndSortedPopupStores(status1, status2);

        for (PopupStoreVO popupStore : filteredAndSortedStores) {
            Long psId = popupStore.getPsId();
            List<String> hashtags = popupStoreService.getHashtagsByPsId(psId);
            popupStore.setHashtags(hashtags);
            log.info("팝업 스토어 정보 해쉬 0 : {}", popupStore);
        }

        model.addAttribute("popupStores", filteredAndSortedStores);
        return "store/storeList :: .store-card";
    }


    // 스토어 검색기능
    @GetMapping("search")
    public String searchStores(@RequestParam("query") String query,
                               @RequestParam("category") String category,
                               Model model) {
        log.info("검색값 : {}", query);
        log.info("카테고리 선택값 : {}", category);

        List<PopupStoreVO> searchResults = popupStoreService.searchStores(query, category);

        for (PopupStoreVO popupStore : searchResults) {
            Long psId = popupStore.getPsId();
            List<String> hashtags = popupStoreService.getHashtagsByPsId(psId);
            popupStore.setHashtags(hashtags);
        }

        log.info("검색된 스토어 : {}", searchResults);

        model.addAttribute("popupStores", searchResults);
        return "store/storeList :: .store-card"; // 결과 부분 업데이트
    }


    // 스토어 상세 페이지로 이동
    @GetMapping("/storeDetail/{psId}")
    public String showStoreDetail(@PathVariable Long psId, Model model,
                                  @AuthenticationPrincipal CustomUser customUser) {

        // 상세페이지로 이동시 조회수 증가시킴
        popupStoreService.increaseViews(psId);

        PopupStoreVO popupStore = popupStoreService.getStoreById(psId); // 스토어 상세정보 가져오기
        List<String> hashtags = popupStoreService.getHashtagsByPsId(psId); // 해당 스토어의 해시태그 가져오기
        List<StorePhotosVO> additionalPhotos = popupStoreService.getAdditionalPhotosByPsId(psId); // 추가 이미지 정보 가져오기

        popupStore.setHashtags(hashtags); // 가져온 해시태그 정보 설정
        popupStore.setAdditionalPhotos(additionalPhotos); // 추가 이미지 정보 설정

        model.addAttribute("storeDetail", popupStore);

        log.info("가져온 팝업정보: {}", popupStore);

        if(customUser != null) {
            log.info("유저 값: {}", customUser);
            model.addAttribute("users", customUser.getUsers());

            // 현재 사용자의 고유번호 가져오기
            Long userId = customUser.getUsers().getUserId();
            // WholikeVO 객체 생성
            WholikeVO likeVO = new WholikeVO();
            likeVO.setPsId(psId);
            likeVO.setUserId(userId);

            // 좋아요 여부 확인
            int isLiked = popupStoreService.isStoreLiked(likeVO);
            model.addAttribute("isLiked", isLiked);
            log.info("좋아요 여부 : {}", isLiked);

        }else {
            log.info("세션에 사용자 정보가 없습니다.");
            // 사용자 세션 정보가 없을 때, 기본 사용자 정보 설정
            Users defaultUser = new Users();
            defaultUser.setUserId(0L); // 기본 사용자 ID
            defaultUser.setNickname("비회원"); // 기본 사용자 이름
            model.addAttribute("users", defaultUser);
            model.addAttribute("isLiked", false); // 기본적으로 좋아요를 하지 않은 상태
        }

        return "store/storeDetail";
    }


    // 팝업스토어 좋아요 업데이트
    @PostMapping("toggleLike/{psId}")
    @ResponseBody
    public ResponseEntity<String> toggleStoreLike(@PathVariable Long psId,
                                                  @AuthenticationPrincipal CustomUser customUser) {
        try {
            if (customUser == null) {
                // 로그인하지 않은 경우, 에러 처리 또는 메시지 반환
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 좋아요를 할 수 있습니다.");
            }
            // 로그인한 유저 고유번호 가져오기
            Long userId = customUser.getUsers().getUserId();

            // 좋아요 여부 확인을 WholikeVO를 사용하여 변경
            WholikeVO likeInfo = new WholikeVO();
            likeInfo.setPsId(psId);
            likeInfo.setUserId(userId);

            int isLiked = popupStoreService.isStoreLiked(likeInfo);

            if (isLiked == 0) {
                // 좋아요를 하지 않은 경우, 좋아요 +1
                popupStoreService.plusStoreLike(psId);

                // 테이블에 좋아요 정보 추가
                likeInfo.setLikeStatus(1); // 좋아요 상태를 1로 설정
                popupStoreService.addLikeInfo(likeInfo); // 정보 추가
            } else {
                // 이미 좋아요를 한 경우, 좋아요 -1
                popupStoreService.minusStoreLike(psId);

                // 테이블에서 좋아요 정보 삭제
                popupStoreService.removeLikeInfo(psId, userId); // 이 부분을 추가로 구현해야 합니다.
            }

            // 업데이트된 좋아요 수 반환
            int updatedLikeCount = popupStoreService.getUpdatedLikeCount(psId);

            return ResponseEntity.ok(Integer.toString(updatedLikeCount)); // 업데이트된 좋아요 수 반환
        } catch (Exception e) {
            e.printStackTrace();
            // 에러 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("좋아요 업데이트 실패");
        }
    }



    // 스토어 수정페이지로 이동
    @GetMapping("/editStore/{psId}")
    public String showEditStorePage(@PathVariable Long psId, Model model,
                                    @AuthenticationPrincipal CustomUser customUser) {
        PopupStoreVO popupStore = popupStoreService.getStoreById(psId); // 스토어 상세정보 가져오기
        List<String> hashtags = popupStoreService.getHashtagsByPsId(psId); // 해당 스토어의 해시태그 가져오기
        List<StorePhotosVO> additionalPhotos = popupStoreService.getAdditionalPhotosByPsId(psId); // 추가 이미지 정보 가져오기

        popupStore.setHashtags(hashtags); // 가져온 해시 태그 정보 설정
        popupStore.setAdditionalPhotos(additionalPhotos); // 추가 이미지 정보 설정

        model.addAttribute("storeDetail", popupStore);

        log.info("수정할 팝업정보: {}", popupStore);

        if(customUser != null) {
            log.info("유저 값: {}", customUser);
            model.addAttribute("users", customUser.getUsers());
        }else {
            log.info("세션에 사용자 정보가 없습니다.");
        }



        return "store/editStore";
    }



    // 수정페이지에서 기존이미지 삭제 (실제파일,DB저장내역 둘다 삭제)
    @PostMapping("removePhoto")
    @ResponseBody
    public ResponseEntity<String> removePhoto(@RequestBody String photoPath) {
        log.info("fileName : {}", photoPath.replace("\"",""));
        try {
            String cleanedPhotoPath = photoPath.replace("\"","");

            // 실제 이미지 파일을 삭제하는 로직 구현
            String additionalImagePath = uploadPath + "additional/" + cleanedPhotoPath;
            Path path = Paths.get(additionalImagePath);

            if (Files.exists(path)) {
                Files.delete(path);

                // 데이터베이스에서 해당 레코드 삭제
                popupStoreService.deleteByPhotopath(cleanedPhotoPath);

                return ResponseEntity.ok("이미지가 성공적으로 삭제되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 이미지를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 삭제 중 오류가 발생했습니다.");
        }
    }





    // 스토어 실시간 댓글 저장
    @PostMapping(value = "realTimeComment", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addRealTimeComment(@RequestBody RealTimeCommentVO realTimeComment) {

        log.info("추가한 댓글정보: {}", realTimeComment);

        try {
            popupStoreService.addRealTimeComment(realTimeComment); // 댓글 등록만

            log.info("댓글이 추가되었습니다.");

            return ResponseEntity.ok().build(); // 성공 시 OK 응답 리턴
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 스토어 실시간 댓글 목록 가져오기
    @GetMapping("getRealTimeComment")
    public ResponseEntity<List<RealTimeCommentVO>> getRealTimeComment(Long psId) {
        log.info("psId : {}", psId);
        // db에서 psId 주고 15개 가져와 리턴
        List<RealTimeCommentVO> result = popupStoreService.getRealTimeCommentsByPsId(psId);

        return new ResponseEntity<>(result, HttpStatus.OK);
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