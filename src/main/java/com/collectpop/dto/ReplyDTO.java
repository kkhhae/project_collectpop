package com.collectpop.dto;

import com.collectpop.domain.FeedReply;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Data
public class ReplyDTO extends FeedReply {
    private String userNickName;
    private String userProfile;
}
