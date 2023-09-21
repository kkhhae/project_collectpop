package com.collectpop.service;

import com.collectpop.domain.*;
import com.collectpop.dto.HashTags;
import com.collectpop.dto.ImgForm;
import com.collectpop.dto.InfiniteScroll;
import com.collectpop.dto.ReplyDTO;
import com.collectpop.repository.FeedRepository;
import com.collectpop.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedServiceImpl implements FeedService{
    private final ImgUpload imgUpload;
    private final FeedRepository feedRepository;
    private final UsersRepository usersRepository;
    @Override
    public List<FeedImg> getFeedLoadData(InfiniteScroll infiniteScroll) {

        return feedRepository.getFeedLoadData(infiniteScroll);
    }

    @Override
    public List<FeedImg> getFidOfFeedImg(Long fid) {
        return feedRepository.getFidOfFeedImg(fid);
    }

    @Override
    public String getFeedContent(Long fid) {
        return feedRepository.getFeedContent(fid);
    }

    @Override
    public Long getFeedLikes(Long fid) {
        return feedRepository.getFeedLikes(fid);
    }

    @Override
    public Long getFeedViews(Long fid) {
        return feedRepository.getFeedViews(fid);
    }

    @Override
    public int CheckLikes(LikesCheck likesCheck) {
        int num = feedRepository.getFeedLikesCheck(likesCheck);
        log.info("service likeCheck num = {}", num);
        if(num == 0){
            log.info("좋아요 누름");
            feedRepository.insertLikesCheck(likesCheck);
            feedRepository.updateFeedLikesUp(likesCheck);
        }else{
            log.info("좋아요 취소");
            feedRepository.deleteLikesCheck(likesCheck);
            feedRepository.updateFeedLikesDown(likesCheck);
        }
        return num;
    }

    @Override
    public void insertReplies(FeedReply feedReply) {
        feedRepository.insertReplies(feedReply);
    }

    @Override
    public List<ReplyDTO> getFidOfReplies(Long fid) {
        List<ReplyDTO> replyDTOS = feedRepository.getFidOfReplies(fid);
        for (ReplyDTO replyDTO : replyDTOS) {
            replyDTO.setUserProfile(usersRepository.getUserIdofuserImg(replyDTO.getUserId()));
            replyDTO.setUserNickName(usersRepository.getUserNickName(replyDTO.getUserId()));
        }
        return replyDTOS;
    }

    @Override
    public Long getFidOfReplyCount(Long fid) {
        return feedRepository.getFidOfReplyCount(fid);
    }

    @Override
    public List<FeedHashTag> getFidOfHashTag(Long fid) {
        return feedRepository.getFidOfHashTag(fid);
    }

    @Override
    public String getFidofFeedRegDate(Long fid) {
        LocalDateTime start = feedRepository.getFidofFeedRegDate(fid);
        LocalDateTime end = LocalDateTime.now();

        long years = ChronoUnit.YEARS.between(start, end);
        long months = ChronoUnit.MONTHS.between(start, end);
        long days = ChronoUnit.DAYS.between(start, end);
        long weeks = days/7;
        long hours = ChronoUnit.HOURS.between(start, end);
        long minutes = ChronoUnit.MINUTES.between(start, end);
        long seconds = ChronoUnit.SECONDS.between(start, end);


        if (years > 0) {
            return years + "년 전";
        } else if (months > 0) {
            return months + "달 전";
        } else if (weeks > 0) {
            return weeks + "주 전";
        } else if (days > 0) {
            return days + "일 전";
        } else if (hours > 0) {
            return hours + "시간 전";
        } else if (minutes > 0) {
            return minutes + "분 전";
        } else if (seconds > 30) {
            return seconds + "초 전";
        } else {
            return "방금 전";
        }
    }

    @Override
    public void deletefeed(Long fid) {
        feedRepository.deleteFeed(fid);
        feedRepository.deleteFeedImg(fid);
        feedRepository.deleteFeedHashTag(fid);
        feedRepository.deleteFeedReply(fid);
        feedRepository.deleteFidOfLikesCheck(fid);
    }

    @Override
    public Long getFeedUserId(Long fid) {
        return feedRepository.getFeedUserId(fid);
    }

    @Override
    public List<FeedImg> getFeedImg() {
        return feedRepository.getFeedImg();
    }

    @Override
    public void insertFeed(ImgForm imgForm) throws IOException {

        List<FeedImg> feedImgs = imgUpload.saveFiles(imgForm.getImageFiles());

        Feed feed = imgForm.toFeed();
        feedRepository.insertFeed(feed);
        Long fid = feed.getFid();

        FeedImgs feedImgList = new FeedImgs();
        feedImgList.setFid(fid);
        feedImgList.setFileName(feedImgs);
        feedRepository.insertFeedImg(feedImgList);
        log.info("service imgForm = {}", imgForm.getTagName());
        HashTags hashTag = imgForm.toHashTag();
        hashTag.setFid(fid);
        feedRepository.insertFeedHashTag(hashTag);

    }

}
