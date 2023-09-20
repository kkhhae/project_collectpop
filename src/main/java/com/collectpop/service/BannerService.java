package com.collectpop.service;

import com.collectpop.domain.Banner;
import com.collectpop.dto.DeleteBannerRequest;

import java.util.List;

public interface BannerService {

    public void insertBanner(Banner banner);

    public List<Banner> getBanner();


    public void deleteBanner(Long bnId);

    Banner getBannerById(Long bnId);
}
