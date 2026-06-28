package com.takeout.xianda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.takeout.xianda.dto.CartDTO;
import com.takeout.xianda.entity.Cart;
import com.takeout.xianda.entity.Product;
import com.takeout.xianda.entity.ProductSku;
import com.takeout.xianda.mapper.CartMapper;
import com.takeout.xianda.mapper.ProductMapper;
import com.takeout.xianda.mapper.ProductSkuMapper;

import com.takeout.xianda.service.CartService;
import com.takeout.xianda.vo.CartVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Override
    public List<CartVO> getUserCart() {
        List<Cart> carts = cartMapper.selectList(new LambdaQueryWrapper<Cart>());
        List<CartVO> collected = carts.stream().map(c -> {
            CartVO vo = new CartVO();
            BeanUtils.copyProperties(c, vo);
            return vo;
        }).collect(Collectors.toList());
        return collected;
    }

    @Override
    public CartVO addUserCart(CartDTO dto) {


        ProductSku productSku = productSkuMapper.selectById(dto.getProductId());
        Product product = productMapper.selectById(productSku.getId());


        CartVO vo = new CartVO();
       vo.setProductId(productSku.getProductId());
       vo.setQuantity(dto.getQuantity());
       vo.setId(product.getId());
       vo.setPrice(productSku.getSalePrice());
       vo.setName(product.getProductName());

        Cart cart = new Cart();
        cart.setQuantity(dto.getQuantity());
        cart.setName(product.getProductName());
        cart.setPrice(productSku.getSalePrice());
        cart.setImage(product.getCoverImage());
        cart.setId(product.getId());
        cart.setProductId(productSku.getProductId());
        cart.setShopId(1);        //TODO    ififif
        cart.setUserId(1);       //TODO
        cart.setCreateTime(LocalDateTime.now());
        cartMapper.insert(cart);

        return vo;

    }

    @Override
    public void updateCartNum(Integer cartItemId, Integer quantity) {
        Cart cart = cartMapper.selectById(cartItemId);
        cart.setQuantity(quantity);
        cartMapper.updateById(cart);
    }

    @Override
    public void deleteCartProduct(Integer cartItemId) {
        cartMapper.deleteById(cartItemId);

    }

    @Override
    public void cleatCart() {
        cartMapper.delete(new LambdaQueryWrapper<Cart>());
    }
}
