package com.collectpop.dto;

import com.collectpop.domain.FeedHashTag;
import com.collectpop.domain.FeedImg;
import com.collectpop.domain.FeedReply;
import lombok.Data;

import java.util.List;

@Data
public class FeedDetail {
    private List<FeedImg> images;
    private String userNickName;
    private String feedContent;
    private String userFileName;
    private Long feedLikes;
    private Long feedViews;
    private Long replyCount;
    private List<FeedHashTag> hashTag;
    private String regDate;
    private Long feedUserId;
    private Boolean userCheck;
}
