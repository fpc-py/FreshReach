package com.takeout.xianda.service;

import com.takeout.xianda.dto.GrabDTO;
import com.takeout.xianda.dto.LoginDTO;
import com.takeout.xianda.entity.Order;
import com.takeout.xianda.vo.IncomeStatsVO;
import com.takeout.xianda.vo.LoginVO;

import java.util.List;

public interface RiderService {
    LoginVO login(LoginDTO dto);

    void onLine(Integer status,Long id);

    List<Order> getAvailableOrders();

    void grabOrders(GrabDTO dto);

    List<Order> getOrders(Integer id);

    void pickupOrders(String id);

    void deliverOrders(String id);

    IncomeStatsVO getIncomeStats(Integer id);
}
