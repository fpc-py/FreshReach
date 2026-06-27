package com.takeout.xianda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.takeout.xianda.entity.Banner;
import com.takeout.xianda.entity.Category;
import com.takeout.xianda.mapper.BannerMapper;
import com.takeout.xianda.mapper.CategoryMapper;
import com.takeout.xianda.service.CategoryService;
import com.takeout.xianda.vo.BannerVO;
import com.takeout.xianda.vo.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BannerMapper bannerMapper;
    @Override
    public List<CategoryVO> getCategories() {
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<>());
        List<CategoryVO> collected = categoryList.stream().map(c -> {
            CategoryVO v = new CategoryVO();
            v.setId(c.getId());
            v.setName(c.getCategoryName());
            v.setIcon(c.getIcon());
            return v;
        }).collect(Collectors.toList());
        return collected;

    }

    @Override
    public List<BannerVO> getBanners() {
        List<Banner> banners = bannerMapper.selectList(new LambdaQueryWrapper<>());
        List<BannerVO> collected = banners.stream().map(b -> {
            BannerVO vo = new BannerVO();
            BeanUtils.copyProperties(b, vo);
            return vo;
        }).collect(Collectors.toList());
        return collected;
    }
}
