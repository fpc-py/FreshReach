package com.takeout.xianda.controller.user;

import com.takeout.xianda.entity.Category;
import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.CategoryService;
import com.takeout.xianda.vo.BannerVO;
import com.takeout.xianda.vo.CategoryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "分类管理")
@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @Operation(summary = "获取分类列表")
    @GetMapping("/categories")
    public Result<List<CategoryVO>> getCategories() {

        List<CategoryVO> list = categoryService.getCategories();
        return Result.success(list);
    }

    @Operation(summary = "获取首页轮播图")
    @GetMapping("/banners")
    public Result<List<BannerVO>> getBanners() {
        List<BannerVO> list = categoryService.getBanners();
        return Result.success(list);
    }
}
