package com.collectpop.repository;

import com.collectpop.domain.*;
import com.collectpop.dto.HashTags;
import com.collectpop.dto.InfiniteScroll;
import com.collectpop.dto.ReplyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FeedRepositoryImpl implements FeedRepository{

    private final FeedMapper feedMapper;

    @Override
    public List<FeedImg> getFeedLoadData(InfiniteScroll infiniteScroll) {
        return feedMapper.getFeedLoadData(infiniteScroll);
    }
    @Override
    public List<FeedImg> getFidOfFeedImg(Long fid) {
        return feedMapper.getFidOfFeedImg(fid);
    }

    @Override
    public void insertFeed(Feed feed) {
        feedMapper.insertFeed(feed);
    }

    @Override
    public void insertFeedImg(FeedImgs feedImgList) {
        List<FeedImg> fileNameList = feedImgList.getFileName();
        for (FeedImg fi: fileNameList) {
            fi.setFid(feedImgList.getFid());
            feedMapper.insertFeedImg(fi);
        }

    }

    @Override
    public void insertFeedHashTag(HashTags hashTags) {
        List<String> tagNameList = hashTags.getHashTags();
        log.info(hashTags.getHashTags().toString());
        FeedHashTag feedHashTag = new FeedHashTag();
        for (String s : tagNameList) {
            log.info("tagNameList = {}", s);
            feedHashTag.setFid(hashTags.getFid());
            feedHashTag.setTagName(s);
            feedMapper.insertFeedHashTag(feedHashTag);
        }

    }

    @Override
    public String getFeedContent(Long fid) {
        return feedMapper.getFeedContent(fid);
    }

    @Override
    public Long getFeedLikes(Long fid) {
        return feedMapper.getFeedLikes(fid);
    }

    @Override
    public Long getFeedViews(Long fid) {
        return feedMapper.getFeedViews(fid);
    }

    @Override
    public int getFeedLikesCheck(LikesCheck likesCheck) {
        return feedMapper.getFeedLikesCheck(likesCheck);
    }

    @Override
    public void updateFeedLikesUp(LikesCheck likesCheck) {
        feedMapper.updateFeedLikesUp(likesCheck);
    }

    @Override
    public void deleteLikesCheck(LikesCheck likesCheck) {
        feedMapper.deleteLikeCheck(likesCheck);
    }

    @Override
    public void insertLikesCheck(LikesCheck likesCheck) {
        feedMapper.insertLikesCheck(likesCheck);
    }

    @Override
    public void insertReplies(FeedReply feedReply) {
        feedMapper.insertReplies(feedReply);
    }

    @Override
    public List<ReplyDTO> getFidOfReplies(Long fid) {
        return feedMapper.getFidOfReplies(fid);
    }

    @Override
    public Long getFidOfReplyCount(Long fid) {
        return feedMapper.getFidOfReplyCount(fid);
    }

    @Override
    public List<FeedHashTag> getFidOfHashTag(Long fid) {
        return feedMapper.getFidOfHashTag(fid);
    }

    @Override
    public LocalDateTime getFidofFeedRegDate(Long fid) {
        return feedMapper.getFidofFeedRegDate(fid);
    }

    @Override
    public void deleteFeed(Long fid) {
        feedMapper.deleteFeed(fid);
    }

    @Override
    public void deleteFeedImg(Long fid) {
        feedMapper.deleteFeedImg(fid);
    }

    @Override
    public void deleteFeedHashTag(Long fid) {
        feedMapper.deleteFeedHashTag(fid);
    }

    @Override
    public void deleteFeedReply(Long fid) {
        feedMapper.deleteFeedReply(fid);
    }

    @Override
    public void deleteFidOfLikesCheck(Long fid) {
        feedMapper.deleteFidOfLikesCheck(fid);
    }

    @Override
    public Long getFeedUserId(Long fid) {
        return feedMapper.getFeedUserId(fid);
    }

    @Override
    public List<Feed> getFeedByNewest() {
        return feedMapper.getFeedByNewest();
    }


    @Override
    public void updateFeedLikesDown(LikesCheck likesCheck) {
        feedMapper.updateFeedLikesDown(likesCheck);
    }

}
