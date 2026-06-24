package com.takeout.xianda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.takeout.xianda.constant.OrderStatusConstant;
import com.takeout.xianda.dto.GrabDTO;
import com.takeout.xianda.dto.LoginDTO;
import com.takeout.xianda.entity.Order;
import com.takeout.xianda.entity.Rider;
import com.takeout.xianda.entity.User;
import com.takeout.xianda.exception.LoginException;
import com.takeout.xianda.mapper.OrderMapper;
import com.takeout.xianda.mapper.RiderMapper;
import com.takeout.xianda.service.RiderService;
import com.takeout.xianda.vo.IncomeStatsVO;
import com.takeout.xianda.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RiderServiceImpl implements RiderService {
    @Autowired
    private OrderMapper  orderMapper;
    private final RiderMapper riderMapper;

    public RiderServiceImpl(RiderMapper riderMapper) {
        this.riderMapper = riderMapper;
    }

    @Override
    public LoginVO login(LoginDTO dto) {

        Rider rider = riderMapper.selectOne(Wrappers.lambdaQuery(Rider.class).eq(Rider::getPhone, dto.getPhone()));
        if ( rider== null) {
            throw new LoginException(4001,"该手机号未注册");
        }
        if (!dto.getPassword().equals(rider.getPassword())) {
            throw new LoginException(4002,"密码错误");
        }
        LoginVO vo = new LoginVO();
        vo.setPhone(dto.getPhone());
        vo.setUsername(rider.getRiderName());
        vo.setUserId(rider.getId());
        vo.setRole(rider.getStatus());
        return vo;

    }

    @Override
    public void onLine(Integer status,Long id) {

        int i = status==1 ? 1 : 0;
        LambdaUpdateWrapper<Rider> wrapper = new LambdaUpdateWrapper<Rider>();
        wrapper.set(Rider::getStatus,i)
                .eq(Rider::getId,id);
        riderMapper.update(null,wrapper);

    }

    @Override
    public List<Order> getAvailableOrders() {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getStatus, OrderStatusConstant.PENDING);
        List<Order> orders = orderMapper.selectList(wrapper);
        return orders;
    }

    @Override
    public void grabOrders(GrabDTO dto) {
        Order order = new Order();
        UpdateWrapper<Order> wrapper = new UpdateWrapper<>();
        wrapper.eq("status","pending").eq("id",dto.getOrderId());
        order.setStatus(OrderStatusConstant.PREPARING);
        order.setRiderId(dto.getId());
        orderMapper.update(order,wrapper);
    }

    @Override
    public List<Order> getOrders(Integer id) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getRiderId,id);
        List<Order> orders = orderMapper.selectList(wrapper);
        return orders;

    }

    @Override
    public void pickupOrders(String id) {
        LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Order::getId,id)
                .set(Order::getStatus,OrderStatusConstant.DELIVERING);
        orderMapper.update(null,wrapper);
    }

    @Override
    public void deliverOrders(String id) {
        LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Order::getId,id)
                .set(Order::getStatus,OrderStatusConstant.COMPLETED);
        orderMapper.update(null,wrapper);
    }

    @Override
    public IncomeStatsVO getIncomeStats(Integer id) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(23, 59, 59);

        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getRiderId,id);
        wrapper.eq(Order::getStatus,OrderStatusConstant.COMPLETED);

        wrapper.between(Order::getCreateTime,start,end);
        List<Order> list = orderMapper.selectList(wrapper);
        IncomeStatsVO statsVO = new IncomeStatsVO();
        statsVO.setTodayOrderCount(Long.valueOf(list.size()));

        BigDecimal sum = list.stream().map(Order::getDeliveryFee)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        statsVO.setTodayIncome(sum);

        LambdaQueryWrapper<Order> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Order::getRiderId,id);
        wrapper1.eq(Order::getStatus,OrderStatusConstant.COMPLETED);
        List<Order> list1 = orderMapper.selectList(wrapper1);
        BigDecimal total = list1.stream().map(Order::getDeliveryFee)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        statsVO.setTotalIncome(total);

        return statsVO;

    }
}
