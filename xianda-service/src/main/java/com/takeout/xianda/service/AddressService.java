package com.takeout.xianda.service;

import com.takeout.xianda.dto.AddressDTO;
import com.takeout.xianda.vo.AddressVO;

import java.util.List;

public interface AddressService {
    List<AddressVO> getAddress();

    void addAddress(AddressDTO dto);

    void updateAddress(Long addressId, AddressDTO dto);

    void deleteAddress(Long addressId);

}
