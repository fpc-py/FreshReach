package com.takeout.xianda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.takeout.xianda.dto.AddressDTO;
import com.takeout.xianda.entity.Address;
import com.takeout.xianda.mapper.AddressMapper;
import com.takeout.xianda.service.AddressService;
import com.takeout.xianda.vo.AddressVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;

    @Override
    public List<AddressVO> getAddress() {
        List<Address> addresses = addressMapper.selectList(new LambdaQueryWrapper<>());
        List<AddressVO> collected = addresses.stream().map(a -> {
            AddressVO vo = new AddressVO();
            vo.setId(a.getId());
            vo.setIsDefault(Boolean.TRUE.equals(a.getIsDefault()));
            vo.setDetail(a.getDetailAddress());
            vo.setName(a.getReceiverName());
            vo.setPhone(a.getReceiverPhone());

            return vo;
        }).collect(Collectors.toList());
        return collected;
    }

    @Override
    public void addAddress(AddressDTO dto) {
        Address address = new Address();
        address.setDetailAddress(dto.getDetail());
        address.setReceiverName(dto.getName());
        address.setReceiverPhone(dto.getPhone());
        address.setId(2L);
        address.setCity(dto.getDetail());
        address.setProvince(dto.getDetail());
        address.setUserId(2L);
        address.setIsDefault(Boolean.TRUE.equals(dto.getIsDefault())? 1:0);
        address.setDistrict("a");
        address.setLongitude(null);
        address.setLatitude(null);       //TODO定位

        addressMapper.insert(address);
    }

    @Override
    public void updateAddress(Long addressId, AddressDTO dto) {
        Address address = new Address();
        address.setDetailAddress(dto.getDetail());
        address.setReceiverName(dto.getName());
        address.setReceiverPhone(dto.getPhone());
        address.setId(2L);
        address.setCity(dto.getDetail());
        address.setProvince(dto.getDetail());
        address.setUserId(2L);
        address.setIsDefault(Boolean.TRUE.equals(dto.getIsDefault())? 1:0);
        address.setDistrict("a");
        address.setLongitude(null);
        address.setLatitude(null);
        addressMapper.updateById(address);


    }

    @Override
    public void deleteAddress(Long addressId) {
        addressMapper.delete(new LambdaQueryWrapper<Address>()
                .eq(Address::getId, addressId));
    }
}
