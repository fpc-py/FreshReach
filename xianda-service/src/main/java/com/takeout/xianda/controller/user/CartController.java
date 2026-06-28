package com.takeout.xianda.controller.user;

import com.takeout.xianda.dto.CartDTO;
import com.takeout.xianda.result.Result;
import com.takeout.xianda.service.CartService;
import com.takeout.xianda.vo.CartVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "购物车")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Operation(summary = "获取用户购物车")
    @GetMapping
    public Result<List<CartVO>> getUserCart(){
      List<CartVO> vo =  cartService.getUserCart();
      return Result.success(vo);

    }

    @Operation(summary = "添加商品到购物车")
    @PostMapping
    public Result<CartVO> addUserCart(@RequestBody CartDTO dto){
        CartVO vo = cartService.addUserCart(dto);
        return Result.success(vo);

    }

    @Operation(summary = "更新购物车商品数量")
    @PutMapping("/{cartItemId}")
    public Result updateCartNum(@PathVariable Integer cartItemId,@RequestParam("quantity")Integer quantity){
        cartService.updateCartNum(cartItemId,quantity);
        return Result.success();
    }


    @Operation(summary = "删除购物车商品")
    @DeleteMapping("/{cartItemId}")
    public Result deleteCartProduct(@PathVariable Integer cartItemId){
        cartService.deleteCartProduct(cartItemId);
        return Result.success();
    }

    @Operation(summary = "清空购物车")
    @DeleteMapping("/clear")
    public Result clearCart(){
        cartService.cleatCart();
        return Result.success();
    }

}
