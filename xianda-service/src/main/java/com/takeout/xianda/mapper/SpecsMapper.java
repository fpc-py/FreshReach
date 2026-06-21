package com.takeout.xianda.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.takeout.xianda.entity.ProductSpecs;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SpecsMapper extends BaseMapper<ProductSpecs> {
}
