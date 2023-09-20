package com.collectpop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

@Data
@AllArgsConstructor
public class InfiniteScroll {
    private int page;
    private int feedSize;
    private String keyword;

    public InfiniteScroll(){
        this(1,12);
    }
    public InfiniteScroll(int page, int feedSize){
        this.page = page;
        this.feedSize = feedSize;
    }
    public String getParameterQuery(){
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
                .queryParam("pageNum", this.page)
                .queryParam("feedSize", this.feedSize)
                .queryParam("keyword", this.keyword);
        return builder.toUriString();
    }


}
