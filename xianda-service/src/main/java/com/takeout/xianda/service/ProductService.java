package com.takeout.xianda.service;

import com.takeout.xianda.dto.CategoryDTO;
import com.takeout.xianda.dto.ProductDTO;
import com.takeout.xianda.dto.ProductPageQueryDTO;
import com.takeout.xianda.entity.Category;
import com.takeout.xianda.entity.Product;
import com.takeout.xianda.result.PageResult;
import com.takeout.xianda.vo.ProductVO;
import com.takeout.xianda.vo.SearchVO;

import java.util.List;

public interface ProductService {
    List<Category> getCategories();

    void addCategory(CategoryDTO dto);

    void updateCategory(CategoryDTO dto);

    void deleteCategory(Long id);

    PageResult getProductList(String productName,Integer page,Integer pageSize);

    Product getById(Long id);

    void save(ProductDTO productDTO);

    void updateProduct(ProductDTO productDTO);

    void deleteProduct(Long id);

    void updateStatus(Long id, Integer status);

    PageResult getProductLists(String category, Integer shopId, Integer page, Integer pageSize);

    ProductVO getProductById(Integer productId);

    List<SearchVO> searchProduct(String keyword);
}
