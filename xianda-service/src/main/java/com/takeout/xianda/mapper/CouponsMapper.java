package com.takeout.xianda.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.takeout.xianda.entity.Coupon;
import com.takeout.xianda.vo.CouponsVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CouponsMapper extends BaseMapper<Coupon> {
}
