package com.takeout.xianda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.takeout.xianda.entity.*;
import com.takeout.xianda.mapper.*;
import com.takeout.xianda.service.UserHomeService;
import com.takeout.xianda.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserHomeServiceImpl implements UserHomeService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    BannerMapper bannerMapper;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Override
    public HomeVO getHomeAllData() {
        HomeVO vo = new HomeVO();

        //1.分类
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<>());
        List<CategoryVO> collected = categoryList.stream().map(c -> {
            CategoryVO v = new CategoryVO();
            v.setId(c.getId());
            v.setName(c.getCategoryName());
            v.setIcon(c.getIcon());
            return v;
        }).collect(Collectors.toList());
        vo.setCategories(collected);


        //2.轮播图
        List<Banner> bannerList = bannerMapper.selectList(new LambdaQueryWrapper<>());
        List<BannerVO> collected1 = bannerList.stream().map(b -> {
            BannerVO v = new BannerVO();
            v.setId(b.getId());
            v.setImage(b.getImage());
            v.setTitle(b.getTitle());
            return v;
        }).collect(Collectors.toList());
        vo.setBanners(collected1);

        //3.筛选标签
        vo.setFilerTabs(Arrays.asList("全部","热销快餐","早餐","下午茶","夜宵"));
        //4.店铺列表
        List<Merchant> merchants = merchantMapper.selectList(new LambdaQueryWrapper<>());
        List<ShopVO> collected2 = merchants.stream().map(m -> {
            ShopVO v = new ShopVO();
            v.setId(m.getId());
            v.setName(m.getName());
            v.setImage(m.getAvatar());
            v.setRating(m.getRating());
            v.setSales(m.getSales());
            v.setDeliveryTime(m.getDeliverTime());
            v.setDeliveryFee(m.getDeliveryFee());
            v.setMinPrice(m.getMinAmount());

            if (m.getNotice() != null) {
                v.setTags(Arrays.asList(m.getNotice().split(",")));

            }
            v.setType("fresh");
            return v;
        }).collect(Collectors.toList());
        vo.setShops(collected2);

        //5.商品列表
        List<Product> productList = productMapper.selectList(new LambdaQueryWrapper<>());
        List<ProductVO> collected3 = productList.stream().map(p -> {
            ProductVO v = new ProductVO();
            v.setId(p.getId());
            v.setName(p.getProductName());
            v.setPrice(null);                //TODO 查价格
            v.setOriginalPrice(null);
            v.setImage(p.getCoverImage());
            v.setSales(p.getStoreId());
            v.setCategory(1);
            return v;
        }).collect(Collectors.toList());
        vo.setProducts(collected3);

        //6.新鲜商品
        List<ProductSku> productSkuList = productSkuMapper.selectList(new LambdaQueryWrapper<>());
        List<FreshProductVO> collected4 = productSkuList.stream().map(s -> {
            FreshProductVO v = new FreshProductVO();
            v.setId(s.getId());
            v.setShopId(null);
            v.setName(s.getSkuName());
            v.setPrice(s.getSalePrice());
            v.setUnit("10"); //TODO
            v.setImage(null);
            return v;
        }).collect(Collectors.toList());
        vo.setFreshProducts(collected4);

        return vo;


    }
}
