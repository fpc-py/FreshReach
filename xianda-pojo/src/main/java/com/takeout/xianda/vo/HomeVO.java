package com.takeout.xianda.vo;

import lombok.Data;

import java.util.List;

@Data
public class HomeVO {
    private List<CategoryVO> categories;
    private List<BannerVO> banners;
    private List<String> filerTabs;
    private List<ShopVO> shops;
    private List<ProductVO> products;
    private List<FreshProductVO> freshProducts;
}
