package com.collectpop.repository;

import com.collectpop.domain.*;
import com.collectpop.dto.InfiniteScroll;
import com.collectpop.dto.ReplyDTO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface FeedMapper {

    List<FeedImg> getFeedLoadData(InfiniteScroll infiniteScroll);

    List<FeedImg> getFidOfFeedImg(Long fid);

    void insertFeed(Feed feed);

    void insertFeedImg(FeedImg feedImgList);

    void insertFeedHashTag(FeedHashTag feedHashTag);

    String getFeedContent(Long fid);

    Long getFeedLikes(Long fid);

    Long getFeedViews(Long fid);

    int getFeedLikesCheck(LikesCheck likesCheck);

    void updateFeedLikesDown(LikesCheck likesCheck);

    void updateFeedLikesUp(LikesCheck likesCheck);

    void deleteLikeCheck(LikesCheck likesCheck);

    void insertLikesCheck(LikesCheck likesCheck);

    void insertReplies(FeedReply feedReply);

    List<ReplyDTO> getFidOfReplies(Long fid);

    Long getFidOfReplyCount(Long fid);

    List<FeedHashTag> getFidOfHashTag(Long fid);

    LocalDateTime getFidofFeedRegDate(Long fid);


    void deleteFeed(Long fid);

    void deleteFeedImg(Long fid);

    void deleteFeedHashTag(Long fid);

    void deleteFeedReply(Long fid);

    void deleteFidOfLikesCheck(Long fid);

    Long getFeedUserId(Long fid);

    List<FeedImg> getFeedImg();
}
