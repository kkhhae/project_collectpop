package com.collectpop.repository;

import com.collectpop.domain.Banner;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BannerMapper {

    public void insertBanner(Banner banner);

    public List<Banner> getBanner();

    public void deleteBanner(Long bnId);

    Banner getBannerById(Long bnId);
}
