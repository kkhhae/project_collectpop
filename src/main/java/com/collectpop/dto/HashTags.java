package com.collectpop.dto;

import com.collectpop.domain.FeedHashTag;
import lombok.Data;

import java.util.List;

@Data
public class HashTags {
    private Long fid;
    private List<String> hashTags;
}
