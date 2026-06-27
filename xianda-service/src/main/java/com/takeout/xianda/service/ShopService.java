package com.takeout.xianda.service;

import com.takeout.xianda.dto.ShopStatusDTO;
import com.takeout.xianda.dto.ShopUpdateDTO;
import com.takeout.xianda.entity.Shop;
import com.takeout.xianda.entity.orderStats;
import com.takeout.xianda.result.PageResult;

import java.util.List;

public interface ShopService {
    List<Shop> getShopInfo();

    void updateShopInfo(ShopUpdateDTO dto);



    void updateShopStatus(ShopStatusDTO dto);

    List<orderStats> getShopStats();

    List<orderStats> getStatsByRange(String startDate,String endDate);

    PageResult getShops(String type,Integer page,Integer pageSize);
}
