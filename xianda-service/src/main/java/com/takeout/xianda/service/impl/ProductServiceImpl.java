package com.takeout.xianda.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.takeout.xianda.entity.Category;
import com.takeout.xianda.entity.Product;
import com.takeout.xianda.mapper.CategoryMapper;
import com.takeout.xianda.mapper.ProductMapper;
import com.takeout.xianda.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> getCategories() {
        LambdaQueryWrapper<Category> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Category::getStatus,1);
        wrapper.orderByAsc(Category::getId);
        return categoryMapper.selectList(wrapper);

    }
}
