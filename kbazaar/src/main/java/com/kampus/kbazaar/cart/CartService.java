package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.cartItem.CartItem;
import com.kampus.kbazaar.cartItem.CartItemService;
import com.kampus.kbazaar.exceptions.NotFoundException;
import com.kampus.kbazaar.cartItem.CartItem;
import com.kampus.kbazaar.shopper.Shopper;
import com.kampus.kbazaar.shopper.ShopperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
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
        List<CartResponse> cartResponseList = new ArrayList<>();
        for (Cart cart: carts){
            List<CartItem> cartItems = cartItemService.findByUsername(cart.getUsername());
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

    public CartItem addToCart(String username, CartRequest request){
        Cart cart = cartRepository.findByUsername(username);
        CartItem item = new CartItem();
        Shopper shopper = shopperRepository.findByUsername(username);
        //item.setId(shopper.getId());
        item.setUsername(shopper.getUsername());
        item.setSku(request.sku());
        item.setName(request.name());
        item.setPrice(request.price());
        item.setQuantity(request.quantity());
        item.setDiscount(request.discount());
        item.setPromotion_codes(request.promotionCodes());
        cartItemRepository.save(item);

        List<CartItem> listItem = cartItemRepository.findAllByUsername(username);
//        BigDecimal discount =0;
//        BigDecimal sumTotal=0;
//        for(CartItem item: listItem ){
//            discount+=item.getDiscount();
//            sumTotal=item.getPrice();
//        }
//        cart.setTotalDiscount(cart.getDiscount()+item.getDiscount());
//        cart.setSubtotal(sumTotal);
//        cart.setGrandTotal(sumTotal-cart.getTotalDiscount());
        cart.setSubtotal(cart.getSubtotal()+item.getPrice());
        cartRepository.save(cart);
        return cart.toResponse(listItem);
}
