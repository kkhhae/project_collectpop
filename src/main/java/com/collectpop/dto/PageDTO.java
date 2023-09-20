package com.collectpop.dto;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@ToString
@Slf4j
public class PageDTO {
    private Long totalBoard;    // 전체 글의 개수
    private int totalPage;      // 전체 페이지 개수
    private int startPage;      // 화면상 첫페이지 번호
    private int endPage;        // 화면상 마지막 페이지 번호
    private boolean prev, next; // 이전, 다음 페이지 존재 여부
    private Pager pager;
    private List<Integer> pageNumList; // 화면에 뿌려줄 페이지 번호 리스트

    public PageDTO(Pager pager, Long totalBoard){
        this.pager = pager;
        this.totalBoard = totalBoard;

        this.endPage = (int)(Math.ceil((double)pager.getPageNum() / pager.getPageSize()) * pager.getPageSize());
        this.startPage = endPage - (pager.getPageSize() - 1);

        totalPage = (int)(Math.ceil((totalBoard.intValue() * 1.0) / pager.getBoardSize()));
        if(totalPage < this.endPage){
            this.endPage = totalPage;
        }
        this.prev = this.startPage > 1;
        this.next = this.endPage < totalPage;

        this.pageNumList = IntStream.rangeClosed(startPage, endPage).boxed().collect(Collectors.toList());

        log.info("Pager = {}", pager);
        log.info("totalPage = {}", totalPage);
        log.info("startPage = {}", startPage);
        log.info("endPage = {}", endPage);
        log.info("prev = {}", prev);
        log.info("next = {}", next);
        log.info("pageNumList = {}", pageNumList);
    }
}

