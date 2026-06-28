package com.takeout.xianda.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeout.xianda.constant.OrderStatusConstant;
import com.takeout.xianda.dto.OrderQueryDTO;
import com.takeout.xianda.dto.UserOrderQueryDTO;
import com.takeout.xianda.entity.Order;
import com.takeout.xianda.entity.OrderItem;
import com.takeout.xianda.entity.Product;
import com.takeout.xianda.exception.BusinessException;
import com.takeout.xianda.mapper.OrderItemMapper;
import com.takeout.xianda.mapper.OrderMapper;
import com.takeout.xianda.mapper.ProductMapper;
import com.takeout.xianda.result.PageResult;

import com.takeout.xianda.service.OrderService;
import com.takeout.xianda.vo.UserOrderAddressVO;
import com.takeout.xianda.vo.UserOrderItemVO;
import com.takeout.xianda.vo.UserOrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class  OrderServiceImpl implements OrderService {
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ProductMapper productMapper;
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

    @Override
    public Page<UserOrderVO> getUserOrderPage(UserOrderQueryDTO dto, Long userId) {
        // 1. 分页条件
        Page<Order> page = new Page<>(dto.getPage(), dto.getPageSize());
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(dto.getStatus())) {
            wrapper.eq(Order::getStatus, dto.getStatus());
        }
        wrapper.orderByDesc(Order::getCreateTime);

        // 2. 查询订单主表
        Page<Order> orderPage = orderMapper.selectPage(page, wrapper);
        List<Order> orderRecords = orderPage.getRecords();
        if(CollUtil.isEmpty(orderRecords)){
            return new Page<>();
        }
        // 3. 批量查询所有订单对应的订单项（避免循环查询N+1）
        List<String> orderIds = orderRecords.stream().map(Order::getId).collect(Collectors.toList());
        List<OrderItem> allOrderItems = orderItemMapper.selectList(
                Wrappers.lambdaQuery(OrderItem.class).in(OrderItem::getOrderId, orderIds)
        );

        // 4. 转换为用户端VO
        List<UserOrderVO> voList = orderRecords.stream().map(order -> {
            UserOrderVO vo = new UserOrderVO();
            BeanUtils.copyProperties(order, vo);
            // 状态文本映射
            vo.setStatusText(getStatusText(order.getStatus()));
            List<Product> list = productMapper.selectList(new LambdaQueryWrapper<Product>().eq(Product::getId, order.getMerchantId()));
            List<UserOrderItemVO> itemVOS = list.stream().map(item -> {
                UserOrderItemVO itemVO = new UserOrderItemVO();
                BeanUtils.copyProperties(item, itemVO);
                return itemVO;
            }).collect(Collectors.toList());
            vo.setItems(itemVOS);
            // 地址封装（从Order实体的收件人、电话字段复制）
            UserOrderAddressVO addressVO = new UserOrderAddressVO();
            addressVO.setName(order.getName());
            addressVO.setPhone(order.getPhone());
            vo.setAddress(addressVO);
            return vo;
        }).collect(Collectors.toList());

        // 5. 组装分页结果
        Page<UserOrderVO> resultPage = new Page<>();
        resultPage.setRecords(voList);
        resultPage.setTotal(orderPage.getTotal());
        resultPage.setPages(orderPage.getPages());
        resultPage.setCurrent(orderPage.getCurrent());
        resultPage.setSize(orderPage.getSize());
        return resultPage;
    }

    // 状态码 -> 中文文本映射工具方法
    private String getStatusText(String status){
        return switch (status) {
            case "pending" -> "待付款";
            case "processing" -> "待接单";
            case "delivering" -> "配送中";
            case "completed" -> "已完成";
            default -> "未知状态";
        };
    }
}

