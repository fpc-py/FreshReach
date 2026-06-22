package com.takeout.xianda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeout.xianda.dto.OrderQueryDTO;
import com.takeout.xianda.entity.Order;
import com.takeout.xianda.mapper.OrderMapper;
import com.takeout.xianda.result.PageResult;
import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class  OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public PageResult getOrderList(OrderQueryDTO dto) {
        Page<Order> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        if (dto.getId() != null) {
            wrapper.eq("id", dto.getId());
        }
        orderMapper.selectPage(page, wrapper);
        return new PageResult(page.getTotal(), page.getRecords());


    }

    @Override
    public Order getOrderDetail(String id) {
        Order order = orderMapper.selectById(id);
        return order;
    }
}
