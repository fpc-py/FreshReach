package com.takeout.xianda.service;

import com.takeout.xianda.vo.BannerVO;
import com.takeout.xianda.vo.CategoryVO;

import java.util.List;

public interface CategoryService {
    List<CategoryVO> getCategories();


    List<BannerVO> getBanners();

}
