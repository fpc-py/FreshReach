package com.takeout.xianda.controller.user;

import com.takeout.xianda.entity.Product;
import com.takeout.xianda.mapper.ProductMapper;
import com.takeout.xianda.result.PageResult;
import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.ProductService;
import com.takeout.xianda.vo.ProductVO;
import com.takeout.xianda.vo.SearchVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品管理")
@RestController
@RequestMapping("/api/productss")
public class ProductsController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductMapper productMapper;

    @Operation(summary = "获取商品列表")
    @GetMapping
    public Result<PageResult> getProductList(@RequestParam("category")String category,
                                             @RequestParam("shopId")Integer shopId,
                                             @RequestParam("page")Integer page,
                                             @RequestParam("pageSize")Integer pageSize){
        PageResult pageResult = productService.getProductLists(category,shopId,page,pageSize);
        return Result.success(pageResult);
    }

    @Operation(summary = "获取商品详情")
    @GetMapping("/{productId}")
    public Result<ProductVO> getProductById(@PathVariable("productId")Integer productId){
        ProductVO list = productService.getProductById(productId);
        return Result.success(list);
    }

    @Operation(summary = "搜索商品")
    @GetMapping("/search")
    public Result<List<SearchVO>> searchProduct(@RequestParam("keyword")String keyword){
        List<SearchVO> vo = productService.searchProduct(keyword);
        return Result.success(vo);
    }

}
