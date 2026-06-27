package com.takeout.xianda.service.impl;

import com.takeout.xianda.result.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeout.xianda.dto.ShopStatusDTO;
import com.takeout.xianda.dto.ShopUpdateDTO;
import com.takeout.xianda.entity.Shop;
import com.takeout.xianda.entity.orderStats;
import com.takeout.xianda.mapper.ShopMapper;
import com.takeout.xianda.mapper.StatsMapper;
import com.takeout.xianda.service.ShopService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private StatsMapper statsMapper;
    @Override
    public List<Shop> getShopInfo() {
        List<Shop> shops = shopMapper.selectList(null);
        return shops;
    }

    @Transactional
    @Override
    public void updateShopInfo(ShopUpdateDTO dto) {

        Shop shop = new Shop();
        BeanUtils.copyProperties(dto,shop);
        shop.setId(1L);
        shopMapper.updateById(shop);
    }

    @Transactional
    @Override
    public void updateShopStatus(ShopStatusDTO dto) {

        int status = Boolean.TRUE.equals(dto.getIsOpen()) ? 1 : 0;
        LambdaUpdateWrapper<Shop> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Shop::getIsOpen, status)
                .set(Shop::getUpdateTime, LocalDateTime.now())
                .eq(Shop::getId,1L);

        shopMapper.update(null,updateWrapper);
    }

    @Override
    public List<orderStats> getShopStats() {
        List<orderStats> list = statsMapper.selectList(null);
        return list;
    }

    @Override
    public List<orderStats> getStatsByRange(String startDate,String endDate) {
        LambdaQueryWrapper<orderStats> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(orderStats::getDate,startDate)
                .le(orderStats::getDate,endDate)
                .orderByDesc(orderStats::getDate);
        return statsMapper.selectList(wrapper);

    }

    @Override
    public PageResult getShops(String type,Integer page,Integer pageSize) {
        Page<Shop> pages = new Page<>(page,pageSize);
        LambdaQueryWrapper<Shop> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Shop::getIsOpen,1);
        wrapper.like(Shop::getType,type);
        shopMapper.selectPage(pages,wrapper);
        return new PageResult(pages.getTotal(),pages.getRecords());
    }


}
