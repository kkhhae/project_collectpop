package com.collectpop.service;

import com.collectpop.domain.*;
import com.collectpop.repository.PopupStoreMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class PopupStoreServiceImpl implements PopupStoreService {

    //생성자 자동주입
    @Autowired
    private PopupStoreMapper popupStoreMapper;

    @Override
    public void addStore(PopupStoreVO popupStore, List<StorePhotosVO> additionalPhotos, String hashtagValues) {

        // 팝업 스토어 정보 저장
        popupStoreMapper.insertStore(popupStore);

        // 등록된 스토어의 ID 가져오기
        Long popupStoreId = popupStore.getPsId();

        // 해시태그를 분리하고 저장
        String[] hashtags = StringUtils.commaDelimitedListToStringArray(hashtagValues);
        for (String hashtag : hashtags) {
            hashtag = hashtag.trim(); // 공백 제거
            if (!hashtag.isEmpty()) {
                PopupStoreHashtagVO hashtagVO = new PopupStoreHashtagVO();
                hashtagVO.setPsId(popupStoreId);
                hashtagVO.setHashtag(hashtag);

                log.info("팝업스토어 고유번호: {}", popupStoreId);
                log.info("팝업스토어 해시태그: {}", hashtag);

                popupStoreMapper.insertHashtag(hashtagVO);
            }
        }

        // 추가 이미지 정보 저장
        for (StorePhotosVO photo : additionalPhotos) {
            String imagePath = photo.getPhotoPath();
            log.info("추가이미지 경로: {}", photo.getPhotoPath());
            if (imagePath != null) {
                photo.setPsId(popupStoreId);
                popupStoreMapper.insertPhoto(photo);
            }
        }

    }

    // 팝업스토어 정보 수정처리 로직
    @Override
    public void editStore(PopupStoreVO updatedStore, List<StorePhotosVO> additionalPhotos, String hashtagValues) {
        try {
            // 1. 스토어 정보 업데이트
            popupStoreMapper.updateStore(updatedStore);

            // 2. 기존 해시태그 삭제
            popupStoreMapper.deleteHashtags(updatedStore.getPsId());

            // 3.등록된 스토어의 ID 가져오기
            Long popupStoreId = updatedStore.getPsId();

            // 4. 새로운 해시태그 추가
            String[] hashtags = StringUtils.commaDelimitedListToStringArray(hashtagValues);
            for (String hashtag : hashtags) {
                hashtag = hashtag.trim(); // 공백 제거
                if (!hashtag.isEmpty()) {
                    PopupStoreHashtagVO hashtagVO = new PopupStoreHashtagVO();
                    hashtagVO.setPsId(popupStoreId);
                    hashtagVO.setHashtag(hashtag);
                    popupStoreMapper.insertHashtag(hashtagVO);
                }
            }

            // 5. 추가 이미지 정보 업데이트
            for (StorePhotosVO photo : additionalPhotos) {
                String imagePath = photo.getPhotoPath();
                if (imagePath != null) {
                    photo.setPsId(popupStoreId);
                    popupStoreMapper.insertPhoto(photo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 에러 처리를 여기에 추가
            throw new RuntimeException("스토어 정보 업데이트 중 오류 발생");
        }
    }

    @Override
    public String getPreviousThumbnailPathByPsId(Long psId) {
        return popupStoreMapper.getPreviousThumbnailPathByPsId(psId);
    }

    @Override
    public String getUserEmailById(Long userId) {

        return popupStoreMapper.getUserEmailById(userId);
    }

    @Override
    public int isStoreLiked(WholikeVO likeVO) {
        return popupStoreMapper.isStoreLiked(likeVO);
    }


    @Override
    public void addLikeInfo(WholikeVO likeInfo) {
        popupStoreMapper.insertLikeInfo(likeInfo);
    }


    @Override
    public void removeLikeInfo(Long psId, Long userId) {
        popupStoreMapper.deleteLikeInfo(psId, userId);
    }

    @Override
    public List<PopupStoreVO> getPopupStoreByUesrId() {
        return popupStoreMapper.getPopupStoreByUesrId();
    }


    @Override
    public void saveRequest(StoreRequestVO storeRequest) {

        popupStoreMapper.insertRequest(storeRequest);
    }

    @Override
    public List<PopupStoreVO> getAllPopupStores() {

        return popupStoreMapper.getAllPopupStores();
    }

    @Override
    public List<String> getHashtagsByPsId(Long psId) {

        return popupStoreMapper.getHashtagsByPsId(psId);
    }

    @Override
    public List<StoreRequestVO> getAllStoreRequests() {

        return popupStoreMapper.getAllStoreRequests();
    }

    @Override
    public List<StoreRequestVO> getAllStoreRequestsSortedByNewest() {
        return popupStoreMapper.getAllStoreRequestsSortedByNewest();
    }

    @Override
    public List<StoreRequestVO> getAllStoreRequestsSortedByOldest() {
        return popupStoreMapper.getAllStoreRequestsSortedByOldest();
    }

    @Override
    public StoreRequestVO getRequestById(Long requestId) {

        return popupStoreMapper.getRequestById(requestId);
    }


    @Override
    public PopupStoreVO getStoreById(Long psId) {
        return popupStoreMapper.getStoreById(psId);
    }

    @Override
    public List<StorePhotosVO> getAdditionalPhotosByPsId(Long psId) {
        return popupStoreMapper.getAdditionalPhotosByPsId(psId);
    }

    @Override
    public void increaseViews(Long psId) {

        popupStoreMapper.increaseViews(psId);
    }

    @Override
    public void plusStoreLike(Long psId) {

        popupStoreMapper.plusStoreLike(psId);
    }

    @Override
    public void minusStoreLike(Long psId) {
        popupStoreMapper.minusStoreLike(psId);
    }

    @Override
    public int getUpdatedLikeCount(Long psId) {
        return popupStoreMapper.getUpdatedLikeCount(psId);
    }

    @Override
    public List<PopupStoreVO> getCurrentPopupStores() {
        return popupStoreMapper.getCurrentPopupStores();
    }

    @Override
    public List<PopupStoreVO> getUpcomingPopupStores() {
        return popupStoreMapper.getUpcomingPopupStores();
    }

    @Override
    public List<PopupStoreVO> getPastPopupStores() {
        return popupStoreMapper.getPastPopupStores();
    }

    @Override
    public List<PopupStoreVO> getStoresSortedByNewest() {
        return popupStoreMapper.getStoresSortedByNewest();
    }

    @Override
    public List<PopupStoreVO> getStoresSortedByOldest() {
        return popupStoreMapper.getStoresSortedByOldest();
    }

    @Override
    public List<PopupStoreVO> getStoresSortedByLikes() {
        return popupStoreMapper.getStoresSortedByLikes();
    }

    @Override
    public List<PopupStoreVO> getStoresSortedByViews() {
        return popupStoreMapper.getStoresSortedByViews();
    }

    @Override
    public List<PopupStoreVO> getFilteredAndSortedPopupStores(String status1, String status2) {

        List<PopupStoreVO> filteredAndSortedStores = new ArrayList<>();

        // status1과 status2에 따라서 필터링된 스토어 목록을 가져옴
        if (status1.equals("current")) {
            filteredAndSortedStores = popupStoreMapper.getCurrentPopupStores();
        } else if (status1.equals("upcoming")) {
            filteredAndSortedStores = popupStoreMapper.getUpcomingPopupStores();
        } else if (status1.equals("past")) {
            filteredAndSortedStores = popupStoreMapper.getPastPopupStores();
        }

        // status2에 따라서 정렬
        if (status2.equals("newest")) {
            Collections.sort(filteredAndSortedStores, Comparator.comparing(PopupStoreVO::getStoreReg).reversed());
        } else if (status2.equals("oldest")) {
            Collections.sort(filteredAndSortedStores, Comparator.comparing(PopupStoreVO::getStoreReg));
        } else if (status2.equals("likes")) {
            Collections.sort(filteredAndSortedStores, Comparator.comparing(PopupStoreVO::getStoreLike).reversed());
        } else if (status2.equals("views")) {
            Collections.sort(filteredAndSortedStores, Comparator.comparing(PopupStoreVO::getViews).reversed());
        }

        return filteredAndSortedStores;
    }

    @Override
    public List<PopupStoreVO> searchStores(String query, String category) {

        if ("title".equals(category)) {
            return popupStoreMapper.searchStoresByTitle(query);
        } else if ("address".equals(category)) {
            return popupStoreMapper.searchStoresByAddress(query);
        } else if ("hashtag".equals(category)) {
            return popupStoreMapper.searchStoresByHashtag(query);
        } else {
            // 기본적으로 전체 검색을 실행하도록 처리
            return popupStoreMapper.searchStores(query);
        }
    }


    // popStore 주소 가져오기
    @Override
    public List<PopupStoreVO> getMap() {
        return popupStoreMapper.getMap();
    }

    @Override
    public List<PopupStoreHashtagVO> getHash() {
        return popupStoreMapper.getHash();
    }




    //실시간 댓글 저장
    @Override
    public void addRealTimeComment(RealTimeCommentVO realTimeComment) {
        popupStoreMapper.insertRealTimeComment(realTimeComment);
    }


    // 팝업 번호로 실시간 댓글정보 가져오기
    @Override
    public List<RealTimeCommentVO> getRealTimeCommentsByPsId(Long psId) {

        return popupStoreMapper.selectRealTimeCommentByPsId(psId);
    }

    @Override
    public void deleteByPhotopath(String cleanedPhotoPath) {
        popupStoreMapper.deleteByPhotopath(cleanedPhotoPath);
    }



}
