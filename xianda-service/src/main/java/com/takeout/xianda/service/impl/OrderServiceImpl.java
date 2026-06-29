package com.takeout.xianda.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeout.xianda.constant.OrderStatusConstant;
import com.takeout.xianda.dto.CreateOrderDTO;
import com.takeout.xianda.dto.OrderItemDTO;
import com.takeout.xianda.dto.OrderQueryDTO;
import com.takeout.xianda.dto.UserOrderQueryDTO;
import com.takeout.xianda.entity.Order;
import com.takeout.xianda.entity.OrderItem;
import com.takeout.xianda.entity.Product;
import com.takeout.xianda.entity.Shop;
import com.takeout.xianda.exception.BusinessException;
import com.takeout.xianda.mapper.*;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private TOrdersMapper tOrdersMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    ShopMapper shopMapper;
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


        Page<Order> page = Page.of(dto.getPage(), dto.getPageSize());
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Order::getUserId, userId);
        if (StrUtil.isNotBlank(dto.getStatus())) {
            wrapper.eq(Order::getStatus, dto.getStatus());
        }
        wrapper.orderByDesc(Order::getCreateTime);
        Page<Order> orderPage = tOrdersMapper.selectPage(page, wrapper);

        if (orderPage.getRecords() == null || orderPage.getRecords().isEmpty()) {
            return new Page<>();
        }

        //收集订单号
        List<String> orderNos = orderPage.getRecords().stream().map(Order::getId).collect(Collectors.toList());

        //批量查明细
        List<OrderItem> itemList = orderItemMapper.selectList(Wrappers.<OrderItem>lambdaQuery().in(OrderItem::getOrderId, orderNos));
        Map<String, List<OrderItem>> itemMap = itemList.stream().collect(Collectors.groupingBy(OrderItem::getOrderId));

        //组装VO
        List<UserOrderVO> voList = orderPage.getRecords().stream().map(order->{
                    UserOrderVO vo = new UserOrderVO();
                    vo.setId(order.getId());  // 订单ID就是订单号
                    vo.setOrderNo(order.getId());
                    vo.setStatus(order.getStatus());
                    vo.setStatusText(STATUS_TEXT_MAP.getOrDefault(order.getStatus(),"未知状态"));
            vo.setShopId(order.getMerchantId());
            Shop shop = shopMapper.selectById(vo.getShopId());
            vo.setShopName(shop != null ? shop.getName() : "");

            vo.setGoodsAmount(order.getTotalAmount());
            vo.setDeliveryFee(order.getDeliveryFee());
            vo.setDiscount(order.getDiscountAmount());
            vo.setPayPrice(order.getPayAmount());
            vo.setTotalPrice(order.getTotalAmount().add(order.getDeliveryFee()));

            vo.setCreateTime(order.getCreateTime() == null ? null
                    : order.getCreateTime());
            vo.setPayTime(order.getPayTime() == null ? null
                    : order.getPayTime());

            // 地址VO
            UserOrderAddressVO address = new UserOrderAddressVO();
            address.setName(order.getName());
            // 手机号脱敏
            if(StrUtil.isNotBlank(order.getPhone()) && order.getPhone().length()>7){
                String phone = order.getPhone();
                address.setPhone(phone.substring(0,3)+"****"+phone.substring(7));
            }else{
                address.setPhone(order.getPhone());
            }
            address.setDetail(StrUtil.join("",order.getProvince(),order.getCity(),order.getDistrict(),order.getDetail()));
            vo.setAddress(address);

            // 明细
            List<OrderItem> items = itemMap.getOrDefault(order.getId(), Collections.emptyList());
            List<UserOrderItemVO> itemVos = items.stream().map(i->{
                UserOrderItemVO itemVO = new UserOrderItemVO();
                itemVO.setId(i.getId());
                itemVO.setProductId(i.getProductId());
                itemVO.setName(i.getName());
                itemVO.setPrice(i.getPrice());
                itemVO.setQuantity(i.getQuantity());
                itemVO.setImage(i.getImage());
                return itemVO;
            }).collect(Collectors.toList());
            vo.setItems(itemVos);
            return vo;
        }).collect(Collectors.toList());

        Page<UserOrderVO> resultPage = new Page<>();
        resultPage.setRecords(voList);
        resultPage.setTotal(orderPage.getTotal());
        resultPage.setCurrent(orderPage.getCurrent());
        resultPage.setSize(orderPage.getSize());
        return resultPage;

    }

    @Override
    public UserOrderVO getUserOrderDetail(String orderId, Long userId) {
        // 1. 使用LambdaQueryWrapper查询订单，确保按订单号查询
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Order::getId, orderId);
        Order order = tOrdersMapper.selectOne(wrapper);
        
        if (order == null) {
            // 查询所有订单看看有哪些
            List<Order> allOrders = tOrdersMapper.selectList(null);
            System.out.println("查询订单失败，orderId: " + orderId);
            System.out.println("数据库中的订单数量: " + (allOrders != null ? allOrders.size() : 0));
            if (allOrders != null && !allOrders.isEmpty()) {
                System.out.println("数据库中的订单ID列表: ");
                for (Order o : allOrders) {
                    System.out.println("  - id: " + o.getId() + ", userId: " + o.getUserId() + ", status: " + o.getStatus());
                }
            }
            throw new BusinessException("订单不存在: " + orderId);
        }

        // 2. 校验订单归属
        if (order.getUserId() != null && !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }

        // 3. 查询订单项
        List<OrderItem> itemList = orderItemMapper.selectList(
                Wrappers.<OrderItem>lambdaQuery().eq(OrderItem::getOrderId, orderId)
        );

        // 4. 查询店铺信息
        Shop shop = shopMapper.selectById(order.getMerchantId());

        // 5. 组装VO
        UserOrderVO vo = new UserOrderVO();
        vo.setId(order.getId());  // 订单ID就是订单号
        vo.setOrderNo(order.getId());
        vo.setStatus(order.getStatus());
        vo.setStatusText(STATUS_TEXT_MAP.getOrDefault(order.getStatus(), "未知状态"));
        vo.setShopId(order.getMerchantId());
        vo.setShopName(shop != null ? shop.getName() : "");

        vo.setGoodsAmount(order.getTotalAmount());
        vo.setDeliveryFee(order.getDeliveryFee());
        vo.setDiscount(order.getDiscountAmount());
        vo.setPayPrice(order.getPayAmount());
        vo.setTotalPrice(order.getTotalAmount().add(order.getDeliveryFee()));

        vo.setCreateTime(order.getCreateTime());
        vo.setPayTime(order.getPayTime());

        // 地址VO
        UserOrderAddressVO address = new UserOrderAddressVO();
        address.setName(order.getName());
        if (StrUtil.isNotBlank(order.getPhone()) && order.getPhone().length() > 7) {
            String phone = order.getPhone();
            address.setPhone(phone.substring(0, 3) + "****" + phone.substring(7));
        } else {
            address.setPhone(order.getPhone());
        }
        address.setDetail(StrUtil.join("", order.getProvince(), order.getCity(), order.getDistrict(), order.getDetail()));
        vo.setAddress(address);

        // 明细
        List<UserOrderItemVO> itemVos = itemList.stream().map(i -> {
            UserOrderItemVO itemVO = new UserOrderItemVO();
            itemVO.setId(i.getId());
            itemVO.setProductId(i.getProductId());
            itemVO.setName(i.getName());
            itemVO.setPrice(i.getPrice());
            itemVO.setQuantity(i.getQuantity());
            itemVO.setImage(i.getImage());
            return itemVO;
        }).collect(Collectors.toList());
        vo.setItems(itemVos);

        return vo;
    }

    // 状态文本映射
    private static final Map<String,String> STATUS_TEXT_MAP = Map.of(
            "pending","待接单",
            "paid","待配送",
            "delivered","配送中",
            "completed","已完成",
            "canceled","已取消"
    );

    @Override
    public String createOrder(CreateOrderDTO dto, Long userId) {
        // 1. 生成订单号
        String orderId = "XD" + System.currentTimeMillis();

        // 2. 计算金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemDTO item : dto.getItems()) {
            totalAmount = totalAmount.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
        }

        // 3. 配送费（满30免配送费）
        BigDecimal deliveryFee = totalAmount.compareTo(new BigDecimal("30")) >= 0
                ? BigDecimal.ZERO : new BigDecimal("3");

        // 4. 计算优惠
        BigDecimal discountAmount = dto.getCouponId() != null ? new BigDecimal("5") : BigDecimal.ZERO;

        // 5. 实付金额
        BigDecimal payAmount = totalAmount.add(deliveryFee).subtract(discountAmount);

        // 6. 创建订单
        Order order = Order.builder()
                .id(orderId)
                .userId(userId)
                .merchantId(1L)  // 或根据商品获取商户ID
                .status("pending")
                .totalAmount(totalAmount)
                .deliveryFee(deliveryFee)
                .discountAmount(discountAmount)
                .payAmount(payAmount)
                .name(dto.getName())
                .phone(dto.getPhone())
                .province(dto.getProvince())
                .city(dto.getCity())
                .district(dto.getDistrict())
                .detail(dto.getDetail())
                .remark(dto.getRemark())
                .createTime(LocalDateTime.now())
                .build();

        tOrdersMapper.insert(order);

        // 7. 创建订单项
        for (OrderItemDTO itemDTO : dto.getItems()) {
            OrderItem item = OrderItem.builder()
                    .orderId(orderId)
                    .productId(itemDTO.getProductId())
                    .name(itemDTO.getName())
                    .price(itemDTO.getPrice())
                    .quantity(itemDTO.getQuantity())
                    .image(itemDTO.getImage())
                    .build();
            orderItemMapper.insert(item);
        }

        return orderId;
    }
    @Override
    public void cancelOrder(String orderId, Long userId, String cancelReason) {
        // 1. 查询订单
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Order::getId, orderId);
        Order order = tOrdersMapper.selectOne(wrapper);

        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 2. 校验订单归属
        if (order.getUserId() != null && !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }

        // 3. 检查订单状态，只有待接单(pending)才能取消
        if (!"pending".equals(order.getStatus())) {
            throw new BusinessException("当前订单无法取消");
        }

        // 4. 更新订单状态为已取消
        order.setStatus("cancelled");
        order.setCancelReason(cancelReason);
        tOrdersMapper.updateById(order);
    }
}

