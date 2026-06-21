package com.takeout.xianda.controller.admin;


import com.takeout.xianda.dto.CategoryDTO;
import com.takeout.xianda.dto.ProductPageQueryDTO;
import com.takeout.xianda.entity.Category;
import com.takeout.xianda.entity.Product;
import com.takeout.xianda.result.PageResult;
import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品管理", description = "商品管理接口")
@RestController
@RequestMapping("api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    //获取商品分类
    @Operation(summary = "获取商品分类",description = "获取商品分类")
    @GetMapping("/categories")
    public Result<List<Category>> getCategories(){

        List< Category> categories = productService.getCategories();
        return Result.success(categories);
    }

    //新增分类
    @Operation(summary = "新增分类",description = "新增分类")
    @PostMapping("/categories")
    public Result addCategory(@RequestBody CategoryDTO dto){
        productService.addCategory(dto);
        return Result.success();
    }

    //更新分类
    @Operation(summary = "更新分类",description = "更新分类")
    @PutMapping("/categories/{id}")
    public Result updateCategory(@PathVariable Long id,@RequestBody CategoryDTO dto){
        dto.setId(id);
        productService.updateCategory(dto);
        return Result.success();
    }

    //删除分类
    @Operation(summary = "删除分类",description = "删除分类")
    @DeleteMapping("/categories/{id}")
    public Result deleteCategory(@PathVariable Long id){
        productService.deleteCategory(id);
        return Result.success();
    }

    //获取商品列表
    @Operation(summary = "获取商品列表",description = "获取商品列表")
    @GetMapping
    public Result<PageResult> getProductList(ProductPageQueryDTO  query){
        PageResult products = productService.getProductList(query);
        return Result.success(products);
    }

}
