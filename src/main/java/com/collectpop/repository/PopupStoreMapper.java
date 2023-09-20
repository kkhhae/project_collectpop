package com.collectpop.repository;

import com.collectpop.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PopupStoreMapper {

    // 팝업스토어 정보 저장
    public void insertStore(PopupStoreVO popupStore);
    // 팝업스토어 해시태그 저장
    public void insertHashtag(PopupStoreHashtagVO popupStoreHashtagVO);
    // 추가 이미지 정보 저장
    public void insertPhoto(StorePhotosVO photo);
    // 팝업스토어 등업신청 내용 저장
    public void insertRequest(StoreRequestVO storeRequest);


    // 팝업스토어 고유번호로 해시태그 가져오기
    public List<String> getHashtagsByPsId(Long psId);


    // 전체 등업신청 리스트 정보 가져오기
    public List<StoreRequestVO> getAllStoreRequests();
    // 등업 리스트 최신순 정렬
    List<StoreRequestVO> getAllStoreRequestsSortedByNewest();
    // 등업 리스트 오래된순 정렬
    List<StoreRequestVO> getAllStoreRequestsSortedByOldest();



    // 등업고유번호로 등업신청정보 가져오기
    public StoreRequestVO getRequestById(Long requestId);
    // 팝업 고유번호로 팝업 상세정보 가져오기
    public PopupStoreVO getStoreById(Long psId);
    // 팝업 고유번호로 추가사진 가져오기
    List<StorePhotosVO> getAdditionalPhotosByPsId(Long psId);


    // 스토어 조회수 증가시키기
    public void increaseViews(Long psId);


    // 스토어 좋아요
    public void plusStoreLike(Long psId);
    // 스토어 좋아요 취소
    public void minusStoreLike(Long psId);
    // 업데이트된 좋아요 수가져오기
    public int getUpdatedLikeCount(Long psId);



    // 전체 팝업스토어 리스트 정보 가져오기
    public List<PopupStoreVO> getAllPopupStores();
    // 현재 오픈 스토어
    public List<PopupStoreVO> getCurrentPopupStores();
    // 오픈 예정 스토어
    public List<PopupStoreVO> getUpcomingPopupStores();
    // 지난 스토어
    public List<PopupStoreVO> getPastPopupStores();

    // 최신 등록순
    public List<PopupStoreVO> getStoresSortedByNewest();
    // 오래된 순
    public List<PopupStoreVO> getStoresSortedByOldest();
    // 좋아요 순
    public List<PopupStoreVO> getStoresSortedByLikes();
    // 조회수 순
    public List<PopupStoreVO> getStoresSortedByViews();



    // 전체로 검색한 스토어 가져오기
    List<PopupStoreVO> searchStores(String query);
    // 제목으로 스토어 검색
    List<PopupStoreVO> searchStoresByTitle(String query);
    // 주소로 스토어 검색
    List<PopupStoreVO> searchStoresByAddress(String query);
    // 해시태그로 스토어 검색
    List<PopupStoreVO> searchStoresByHashtag(String query);




    // 지도 불러오기
    public List<PopupStoreVO> getMap();

    // 해시태그 불러오기
    public List<PopupStoreHashtagVO> getHash();




    // 실시간 댓글 저장
    public void insertRealTimeComment(RealTimeCommentVO realTimeComment);

    // 댓글 고유번호로 댓글 가져오기
    public RealTimeCommentVO selectRealTimeCommentById(Long commentId);

    // 팝업 고유번호로 댓글 가져오기
    public List<RealTimeCommentVO> selectRealTimeCommentByPsId(Long psId);

    // 실제 추가된 이미지파일 지우고 테이블에서 지우기
    public void deleteByPhotopath(String cleanedPhotoPath);

    // 스토어 정보 업데이트
    public void updateStore(PopupStoreVO updatedStore);

    // 수정 전 기존 해시태그 목록 지우기
    public void deleteHashtags(Long psId);

    // 팝업 고유번호로 대표사진 파일명 가져오기
    public String getPreviousThumbnailPathByPsId(Long psId);

    // 유저 고유번호로 유저 이메일값 받아오기
    public String getUserEmailById(Long userId);

    // 좋아요 여부 확인용
    public int isStoreLiked(WholikeVO likeVO);

    // 좋아요 정보 추가
    public void insertLikeInfo(WholikeVO likeInfo);
    // 좋아요 정보 삭제
    public void deleteLikeInfo(@Param("psId") Long psId, @Param("userId") Long userId);

    List<PopupStoreVO> getPopupStoreByUesrId();
}