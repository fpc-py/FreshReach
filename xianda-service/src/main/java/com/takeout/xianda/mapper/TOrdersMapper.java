package com.takeout.xianda.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeout.xianda.entity.Order;
import com.takeout.xianda.vo.UserOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TOrdersMapper extends BaseMapper<Order>{
    IPage<UserOrderVO> selectUserOrdersList(@Param("page") Page<Order> page,
                                            @Param("userId")Long userId,
                                            @Param("status")String status);
}
