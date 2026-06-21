package com.takeout.xianda.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeout.xianda.dto.CategoryDTO;
import com.takeout.xianda.dto.ProductDTO;
import com.takeout.xianda.dto.ProductPageQueryDTO;
import com.takeout.xianda.entity.Category;
import com.takeout.xianda.entity.Product;
import com.takeout.xianda.entity.ProductSpecs;
import com.takeout.xianda.mapper.CategoryMapper;
import com.takeout.xianda.mapper.ProductMapper;
import com.takeout.xianda.mapper.SpecsMapper;
import com.takeout.xianda.result.PageResult;
import com.takeout.xianda.service.ProductService;
import com.takeout.xianda.vo.ProductPageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private SpecsMapper specsMapper;

    @Override
    public List<Category> getCategories() {
        LambdaQueryWrapper<Category> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Category::getStatus,1);
        wrapper.orderByAsc(Category::getId);
        return categoryMapper.selectList(wrapper);

    }

    @Override
    public void addCategory(CategoryDTO dto) {
        Category category = new Category();
        category.setCategoryName(dto.getCategoryName());
        category.setStatus(1);
        categoryMapper.insert(category);

    }

    @Override
    public void updateCategory(CategoryDTO dto) {
        Category category = new Category();
        BeanUtils.copyProperties(dto,category);
        categoryMapper.updateById(category);
    }

    @Override
    public void deleteCategory(Long id) {

        categoryMapper.deleteById(id);
    }

    @Override
    public PageResult getProductList(ProductPageQueryDTO query) {
        Page<ProductPageVO> page = new Page<>(query.getPageNum(),query.getPageSize());
        IPage<ProductPageVO> pageVO = productMapper.selectProductPageJoinSku(page, query);
        return new PageResult(pageVO.getTotal(),pageVO.getRecords());

    }

    @Override
    public Product getById(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    @Transactional
    public void save(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO,product,"specs");
        product.setStoreId(1);
        productMapper.insert(product);
        Long pid = product.getId();

        List<ProductSpecs> specs = productDTO.getSpecs();
        if (specs != null && !specs.isEmpty()){
            List<ProductSpecs> list = specs.stream().map(item -> {
                ProductSpecs specs1 = new ProductSpecs();
                specs1.setProductId(pid);
                specs1.setSpecsName(item.getSpecsName());
                String options = String.join(",", item.getSpecsOptions());
                specs1.setSpecsOptions(options);
                return specs1;
            }).collect(Collectors.toList());
            for (ProductSpecs spec : list) {
                specsMapper.insert(spec);
            }

        }


    }
}
