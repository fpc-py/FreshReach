package com.takeout.xianda.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.takeout.xianda.dto.ProductPageQueryDTO;
import com.takeout.xianda.entity.Product;
import com.takeout.xianda.vo.ProductPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    IPage<ProductPageVO> selectProductPageJoinSku(IPage<ProductPageVO> page, @Param("query") ProductPageQueryDTO query);



}
