package com.collectpop.controller;

import com.collectpop.domain.*;
import com.collectpop.dto.*;
import com.collectpop.security.CustomUser;
import com.collectpop.service.FeedService;
import com.collectpop.service.ImgUpload;
import com.collectpop.service.UsersService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/collectpop")
@RequiredArgsConstructor
public class FeedController {
    private final ImgUpload imgUpload;
    private final FeedService feedService;
    private final UsersService usersService;

    @GetMapping("/feed")
    public String feedForm(Model model, @AuthenticationPrincipal CustomUser customUser) {
        if(customUser != null) {
            log.info("유저 값: {}", customUser);
            model.addAttribute("users", customUser.getUsers());
        }else {
            log.info("세션에 사용자 정보가 없습니다.");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        String fileName = usersService.getuserImg(userName);
        String nickName = usersService.getUserName(userName);
        Long userId = usersService.getUserId(userName);
        log.info("nickName == {}", nickName);
        log.info("fileName == {}", fileName);
        log.info("userId == {}",userId);
        model.addAttribute("nickName",nickName);
        model.addAttribute("fileName",fileName);
        model.addAttribute("userId", userId);

        return "feed/feed";
    }

    @GetMapping("/items")
    public ResponseEntity<List<FeedImg>> itemsloading(InfiniteScroll infiniteScroll) {
        log.info("infiniteScroll keyWord = {}", infiniteScroll.getKeyword());
        List<FeedImg> loadData = feedService.getFeedLoadData(infiniteScroll);
        log.info("loadData = {}",loadData);
        return new ResponseEntity<>(loadData, HttpStatus.OK);
    }
    //업로드에 관한 피드 받아오는 컨트롤러
    @PostMapping("/addfeed")
    public ResponseEntity<String> addfeedimg(
            @RequestParam("userId")Long userId,
            @RequestParam("content")String content,
            @RequestParam("values")String values,
            @RequestParam("imageFiles")MultipartFile[] imageFiles) throws IOException {
        log.info("userId = {}", userId);
        log.info("content = {}", content);
        log.info("values = {}", values);
        log.info("imageFiles = {}", (Object[]) imageFiles);
        List<String> tagName = new ArrayList<>();
        String[] st = values.split(",");
        Collections.addAll(tagName, st);
        ImgForm imgForm = new ImgForm();
        imgForm.setUserId(userId);
        imgForm.setContent(content);
        imgForm.setTagName(tagName);
        imgForm.setImageFiles(List.of(imageFiles));
        feedService.insertFeed(imgForm);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/getFeedImg")
    public ResponseEntity<FeedDetail> getFeedImg(Long fid){
        log.info("fid == {}", fid);
        boolean usercheck = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Long sessionId = usersService.getUserId(userName);
        Long userId = feedService.getFeedUserId(fid);
        if(Objects.equals(sessionId, userId)){
            usercheck = true;
        }
        String nickName = usersService.getUserNickName(userId);
        String userImg = usersService.getUserIdofuserImg(userId);
        log.info("userImg == {}",userImg);
        log.info("userId == {}", userId);
        log.info("nickName == {}", nickName);
        Long feedLikes = feedService.getFeedLikes(fid);
        FeedDetail result = new FeedDetail();
        result.setUserCheck(usercheck);
        result.setImages(feedService.getFidOfFeedImg(fid));
        result.setFeedContent(feedService.getFeedContent(fid));
        result.setUserFileName(userImg);
        result.setFeedLikes(feedLikes);
        log.info("feedLikes = {}", feedLikes);
        result.setFeedViews(feedService.getFeedViews(fid));
        result.setReplyCount(feedService.getFidOfReplyCount(fid));
        result.setHashTag(feedService.getFidOfHashTag(fid));
        result.setFeedUserId(userId);
        result.setUserNickName(nickName);
        result.setRegDate(feedService.getFidofFeedRegDate(fid));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/likes")
    public ResponseEntity<Integer> likes(LikesCheck likesCheck){
        log.info("likesCheck = {}", likesCheck);
        int result = feedService.CheckLikes(likesCheck);
        log.info("result return = {}", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/replies")
    public ResponseEntity<String> replies(FeedReply feedReply){
        log.info("feedReply = {}",feedReply);
        feedService.insertReplies(feedReply);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
    @GetMapping("/getReplies")
    public ResponseEntity<List<ReplyDTO>> getReplies(Long fid){
        log.info("getReplies fid = {}" , fid);
        List<ReplyDTO> replyDTOS = feedService.getFidOfReplies(fid);
        return new ResponseEntity<>(replyDTOS, HttpStatus.OK);
    }
    @PostMapping("/deletefeed")
    public ResponseEntity<String> deletefeed(Long fid){
        feedService.deletefeed(fid);
        return new ResponseEntity<>("deleteSuccess", HttpStatus.OK);
    }
    //이미지 화면에 전달 할 때 필요한 이미지 요청 경로
    @ResponseBody
    @GetMapping("/images/{orgFileName}")
    public Resource getImages(@PathVariable String orgFileName) throws MalformedURLException {
        return new UrlResource("file:" + imgUpload.getFilePath(orgFileName));
    }
    @ResponseBody
    @GetMapping("/userimages/{orgFileName}")
    public Resource getUserImg(@PathVariable String orgFileName) throws MalformedURLException {
        String userFilePath = imgUpload.getUserFilePath(orgFileName);
        return new UrlResource("file:" + userFilePath);
    }
}
