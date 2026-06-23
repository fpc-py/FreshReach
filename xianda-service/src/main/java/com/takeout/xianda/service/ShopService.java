package com.takeout.xianda.service;

import com.takeout.xianda.dto.ShopStatusDTO;
import com.takeout.xianda.dto.ShopUpdateDTO;
import com.takeout.xianda.entity.Shop;

import java.util.List;

public interface ShopService {
    List<Shop> getShopInfo();

    void updateShopInfo(ShopUpdateDTO dto);



    void updateShopStatus(ShopStatusDTO dto);
}
