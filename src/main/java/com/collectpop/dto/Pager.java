package com.collectpop.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

@Data
@Slf4j
public class Pager {

    private int pageNum;    // 페이지 번호
    private int boardSize;  // 한 페이지당 보여줄 글 개수
    private int pageSize;   // 페이지 번호 띄워줄 개수
    private String keyword; // 검색 키워드
    private String searchType; // 검색 유형 (기준) t, c ,w, tw, tc, cw

    public Pager(){
        this(1, 10, 10);
    }
    public Pager(int pageNum, int boardSize, int pageSize){
        this.pageNum = pageNum;
        this.boardSize = boardSize;
        this.pageSize = pageSize;
    }
    public Pager(int pageNum, int boardSize, int pageSize, String keyword, String searchType){
        this.pageNum = pageNum;
        this.boardSize = boardSize;
        this.pageSize = pageSize;
        this.keyword = keyword;
        this.searchType = searchType;
    }
    public String[] getSearchTypeArr(){
        return searchType == null ? new String[]{} : searchType.split("");
    }
    // 위 변수값들을 쿼리스트링으로 변환 해주는 메서드
    public String getParameterQuery(){
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
                .queryParam("pageNum", this.pageNum)
                .queryParam("boardSize", this.boardSize)
                .queryParam("pageSize", this.pageSize)
                .queryParam("keyword", this.keyword)
                .queryParam("searchType", this.searchType);
            log.info("Pager uri QueryString = {}", builder.toUriString());
        return builder.toUriString();
    }
}
