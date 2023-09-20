package com.collectpop.service;

import com.collectpop.domain.Banner;
import com.collectpop.dto.DeleteBannerRequest;
import com.collectpop.repository.BannerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class BannerServiceImpl implements  BannerService{

    private final BannerMapper bannerMapper;

    @Override
    public void insertBanner(Banner banner) {
        bannerMapper.insertBanner(banner);
    }

    @Override
    public List<Banner> getBanner() {
        return bannerMapper.getBanner();
    }

    @Override
    public void deleteBanner(Long bnId) {
        bannerMapper.deleteBanner(bnId);
    }

    @Override
    public Banner getBannerById(Long bnId) {
        return bannerMapper.getBannerById(bnId);
    }


}
