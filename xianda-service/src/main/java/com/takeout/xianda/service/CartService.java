package com.takeout.xianda.service;

import com.takeout.xianda.dto.CartDTO;
import com.takeout.xianda.vo.CartVO;

import java.util.List;

public interface CartService {
    List<CartVO> getUserCart();

    CartVO addUserCart(CartDTO dto);

    void updateCartNum(Integer cartItemId, Integer quantity);

    void deleteCartProduct(Integer cartItemId);

    void cleatCart();
}
