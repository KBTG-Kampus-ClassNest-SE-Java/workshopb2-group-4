package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.cartItem.CartItem;
import com.kampus.kbazaar.cartItem.CartItemService;
import com.kampus.kbazaar.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    private CartRepository cartRepository;
    private CartItemService cartItemService;

    public CartService(CartRepository cartRepository, CartItemService cartItemService) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
    }

    public List<CartResponse> getAll(){
        List<Cart> carts = cartRepository.findAll();
        if(carts.isEmpty()){
            throw new NotFoundException("Cart not found");
        }

//        CartResponse cartResponse = new CartResponse();
        List<CartResponse> cartResponseList = new ArrayList<CartResponse>();
        for (Cart cart: carts){
            List<CartItem> cartItems = cartItemService.findByUsername(cart.getUsername());
            //Calculate for each username
//            BigDecimal discount = 0;
//            for (CartItem cartItem: cartItems){
//
//            }
            CartResponse cartResponse = new CartResponse(
                    cart.getUsername(),
                    cartItems,
                    cart.getDiscount(),
                    cart.getTotalDiscount(),
                    cart.getSubtotal(),
                    cart.getGrandTotal()
            );
            cartResponseList.add(cartResponse);
        }

        return cartResponseList;
    }
}
