package com.collectpop.service;

import com.collectpop.domain.*;
import com.collectpop.dto.ImgForm;
import com.collectpop.dto.InfiniteScroll;
import com.collectpop.dto.ReplyDTO;

import java.io.IOException;
import java.util.List;

public interface FeedService {
    List<FeedImg> getFeedLoadData(InfiniteScroll infiniteScroll);

    void insertFeed(ImgForm imgForm) throws IOException;

    List<FeedImg> getFidOfFeedImg(Long fid);

    String getFeedContent(Long fid);

    Long getFeedLikes(Long fid);

    Long getFeedViews(Long fid);

    int CheckLikes(LikesCheck likesCheck);

    void insertReplies(FeedReply feedReply);

    List<ReplyDTO> getFidOfReplies(Long fid);

    Long getFidOfReplyCount(Long fid);

    List<FeedHashTag> getFidOfHashTag(Long fid);

    String getFidofFeedRegDate(Long fid);

    void deletefeed(Long fid);

    Long getFeedUserId(Long fid);

    List<FeedImg> getFeedImg();
}
