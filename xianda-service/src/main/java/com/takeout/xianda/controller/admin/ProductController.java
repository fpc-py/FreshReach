package com.takeout.xianda.controller.admin;


import com.takeout.xianda.entity.Category;
import com.takeout.xianda.entity.Product;
import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "商品管理", description = "商品管理接口")
@RestController
@RequestMapping("api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    //获取商品分类
    @GetMapping("/categories")
    public Result<List<Category>> getCategories(){

        List< Category> categories = productService.getCategories();
        return Result.success(categories);
    }
}
