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
import com.takeout.xianda.vo.ProductVO;
import com.takeout.xianda.vo.SearchVO;
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
    public PageResult getProductList(String productName,Integer page,Integer pageSize) {
        Page<ProductPageVO> pages = new Page<>(page,pageSize);
        IPage<ProductPageVO> pageVO = productMapper.selectProductPageJoinSku(pages, productName);
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

    @Override
    public void updateProduct(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO,product);
        productMapper.updateById(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productMapper.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        //builder
        Product product = Product.builder()
                .id(id)
                .status(status)
                .build();
        productMapper.updateById(product);
    }

    @Override
    public PageResult getProductLists(String category, Integer shopId, Integer page, Integer pageSize) {
        Page<Product> page1 = new Page<>(page,pageSize);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getCategoryId,category);
        productMapper.selectPage(page1,wrapper);
        return new PageResult(page1.getTotal(),page1.getRecords());


    }

    @Override
    public ProductVO getProductById(Integer productId) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getId,productId);
        Product product = productMapper.selectOne(wrapper);
        ProductVO vo = new ProductVO();
        vo.setId(product.getId());
        vo.setImage(product.getCoverImage());
        vo.setSales(product.getStoreId());
        vo.setName(product.getProductName());
        vo.setCategory(product.getCategoryId());
        return vo;
    }

    @Override
    public List<SearchVO> searchProduct(String keyword) {

        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Product::getProductName,keyword);
        List<Product> productList = productMapper.selectList(wrapper);
        List<SearchVO> collected = productList.stream().map(p -> {
            SearchVO vo = new SearchVO();
            vo.setId(p.getId());
            vo.setName(p.getProductName());
            vo.setImage(p.getCoverImage());
            vo.setPrice(null);   //TODO sku 查价格
            return vo;

        }).collect(Collectors.toList());


        return collected;

    }
}
