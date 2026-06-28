package com.takeout.xianda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.takeout.xianda.entity.Coupon;
import com.takeout.xianda.mapper.CouponsMapper;
import com.takeout.xianda.service.CouponsService;
import com.takeout.xianda.vo.CouponsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponsServiceImpl implements CouponsService {
    @Autowired
    private CouponsMapper couponsMapper;

    @Override
    public List<CouponsVO> getUserCoupons() {

        List<Coupon> list = couponsMapper.selectList(new LambdaQueryWrapper<Coupon>());
        List<CouponsVO> collect = list.stream().map(l -> {
            CouponsVO vo = new CouponsVO();
            BeanUtils.copyProperties(l, vo);
            return vo;
        }).collect(Collectors.toList());
        return collect;
    }
}
