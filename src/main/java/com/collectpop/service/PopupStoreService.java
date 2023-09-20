package com.collectpop.service;

import com.collectpop.domain.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface PopupStoreService {

    // 팝업스토어 등록
    public void addStore(PopupStoreVO popupStore, List<StorePhotosVO> additionalPhotos, String hashtagValues);

    // 팝업스토어 등업 신청내용 저장
    public void saveRequest(StoreRequestVO storeRequest);

    // 팝업 고유번호로 해시태그 가져오기
    public List<String> getHashtagsByPsId(Long psId);

    // 전체 등업 신청 리스트 정보 가져오기
    public List<StoreRequestVO> getAllStoreRequests();
    // 등업리스트 최신순 정렬
    List<StoreRequestVO> getAllStoreRequestsSortedByNewest();
    // 등업리스트 오래된순 정렬
    List<StoreRequestVO> getAllStoreRequestsSortedByOldest();

    // 등업 고유번호에 맞는 신청정보 가져오기
    public StoreRequestVO getRequestById(Long requestId);

    // 팝업 고유번호에 맞는 팝업 상세정보 가져오기
    public PopupStoreVO getStoreById(Long psId);

    // 팝업 고유번호로 추가 이미지 가져오기
    public List<StorePhotosVO> getAdditionalPhotosByPsId(Long psId);

    // 스토어 조회수 증가시키기
    public void increaseViews(Long psId);


    // 스토어 좋아요
    public void plusStoreLike(Long psId);
    // 스토어 좋아요 취소
    public void minusStoreLike(Long psId);
    // 업데이트된 좋아요 수 가져오기
    public int getUpdatedLikeCount(Long psId);


    // 전체 팝업스토어 리스트 정보 가져오기
    public List<PopupStoreVO> getAllPopupStores();
    // 현재 오픈 스토어
    public List<PopupStoreVO> getCurrentPopupStores();
    // 오픈 예정 스토어
    public List<PopupStoreVO> getUpcomingPopupStores();
    // 지난 스토어
    public List<PopupStoreVO> getPastPopupStores();

    // 최신순
    public List<PopupStoreVO> getStoresSortedByNewest();
    // 오래된순
    public List<PopupStoreVO> getStoresSortedByOldest();
    // 좋아요순
    public List<PopupStoreVO> getStoresSortedByLikes();
    // 조회수순
    public List<PopupStoreVO> getStoresSortedByViews();

    // 3번 로직구현
    List<PopupStoreVO> getFilteredAndSortedPopupStores(String status1, String status2);

    // 검색기능
    List<PopupStoreVO> searchStores(String query, String category);

    // 지도 불러오기
    public List<PopupStoreVO> getMap();

    public List<PopupStoreHashtagVO> getHash();


    // 스토어 실시간 댓글 저장
    public void addRealTimeComment(RealTimeCommentVO realTimeComment);


    // 팝업 고유번호로 저장되어있는 댓글 정보 15개 가져오기
    List<RealTimeCommentVO> getRealTimeCommentsByPsId(Long psId);


    // 실제 이미지파일 삭제후 테이블 저장값도 제거
    public void deleteByPhotopath(String cleanedPhotoPath);

    // 스토어 수정 값 업데이트
    public void editStore(PopupStoreVO updatedStore, List<StorePhotosVO> additionalPhotos, String hashtagValues);

    // 팝업 고유번호로 대표사진 파일명 가져오기
    public String getPreviousThumbnailPathByPsId(Long psId);
    
    // 유저 고유번호로 유저이메일값 가져오기
    public String getUserEmailById(Long userId);

    // 좋아요 여부 확인하기
    public int isStoreLiked(WholikeVO likeVO);

    // 좋아요 정보 추가
    public void addLikeInfo(WholikeVO likeInfo);
    // 좋아요 정보 삭제
    public void removeLikeInfo(Long psId, Long userId);

    List<PopupStoreVO> getPopupStoreByUesrId();
}