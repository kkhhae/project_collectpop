package com.collectpop.domain;

import lombok.Data;

import java.util.List;

@Data
public class FeedImgs {
    private Long fiid;
    private Long fid;
    private List<FeedImg> fileName;
}
