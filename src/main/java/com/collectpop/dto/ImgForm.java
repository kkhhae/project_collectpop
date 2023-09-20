package com.collectpop.dto;

import com.collectpop.domain.Feed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@ToString
public class ImgForm {
    private Long userId;
    private String content;
    private List<String> tagName;
    private List<MultipartFile> imageFiles;

    public Feed toFeed(){
        Feed feed = new Feed();
        feed.setUserId(this.userId);
        feed.setContent(this.content);
        return feed;
    };
    public HashTags toHashTag(){
        HashTags ht = new HashTags();
        ht.setHashTags(this.tagName);
        return ht;
    }
}

