package com.takeout.xianda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeout.xianda.constant.OrderStatusConstant;
import com.takeout.xianda.dto.OrderQueryDTO;
import com.takeout.xianda.entity.Order;
import com.takeout.xianda.exception.BusinessException;
import com.takeout.xianda.mapper.OrderMapper;
import com.takeout.xianda.result.PageResult;

import com.takeout.xianda.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    //接单
    @Override
    @Transactional
    public void acceptOrder(String id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!OrderStatusConstant.PENDING.equals(order.getStatus())) {
            throw new BusinessException("当前订单不可接单");
        }
        order.setStatus(OrderStatusConstant.PREPARING);
            orderMapper.updateById(order);
        }

    @Override
    @Transactional
    public void rejectOrder(String id, String cancelReason) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!OrderStatusConstant.PENDING.equals(order.getStatus())) {
            throw new BusinessException("当前订单不可拒单");
        }
        order.setStatus(OrderStatusConstant.CANCELLED);
        order.setCancelReason(cancelReason);
     orderMapper.updateById(order);
    }

    @Override
    public void finishPreparing(String id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        order.setStatus(OrderStatusConstant.DELIVERING);
        orderMapper.updateById(order);
    }

    @Override
    public void agreeRefund(String id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        order.setStatus(OrderStatusConstant.REFUNDED);
        orderMapper.updateById(order);
    }

    @Override
    public void rejectRefund(String id, String cancelReason) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        order.setStatus(OrderStatusConstant.REFUNDED);
        order.setCancelReason(cancelReason);
        orderMapper.updateById(order);
    }
}

