package com.collectpop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedImg {
    private Long fiid;
    private Long fid;
    private String orgFileName;
    private String fileName;
    public FeedImg(String orgFileName, String storedFileName){
        this.orgFileName = orgFileName;
        this.fileName = storedFileName;
    }
}
