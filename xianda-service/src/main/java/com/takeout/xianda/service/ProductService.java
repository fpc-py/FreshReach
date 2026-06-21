package com.takeout.xianda.service;

import com.takeout.xianda.dto.CategoryDTO;
import com.takeout.xianda.entity.Category;
import com.takeout.xianda.entity.Product;

import java.util.List;

public interface ProductService {
    List<Category> getCategories();

    void addCategory(CategoryDTO dto);

    void updateCategory(CategoryDTO dto);

    void deleteCategory(Long id);
}
