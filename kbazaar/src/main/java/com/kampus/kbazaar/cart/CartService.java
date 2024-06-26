package com.kampus.kbazaar.cart;
import com.kampus.kbazaar.cartItem.CartItemRepository;
import com.kampus.kbazaar.cartItem.CartItemService;
import com.kampus.kbazaar.exceptions.NotFoundException;
import com.kampus.kbazaar.cartItem.CartItem;
import org.springframework.beans.factory.annotation.Value;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;



@Service
public class CartService {
    private CartRepository cartRepository;
    private CartItemService cartItemService;
    private CartItemRepository cartItemRepository;

    @Value("${enabled.shipping.fee}")
    private String enableShippingFee;

    public CartService(CartRepository cartRepository, CartItemService cartItemService, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartResponse> getAll() {
        List<Cart> carts = cartRepository.findAll();
        if (carts.isEmpty()) {
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

    @Transactional
    public CartResponse addToCart(String username, CartRequest request) {
        Cart cart = cartRepository.findByUsername(username);
        CartItem item = new CartItem();
        //item.setId(shopper.getId());
        item.setUsername(username);
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
        if (enableShippingFee.equals("true")){
            cart.setShippingFee(BigDecimal.valueOf(25.0));
        } else {
            cart.setShippingFee(BigDecimal.valueOf(0));
        }
        cart.setSubtotal(cart.getSubtotal().add(item.getPrice()));
        cart.setGrandTotal(cart.getSubtotal().add(cart.getShippingFee()));
        cartRepository.save(cart);
        return cart.toResponse(listItem);
    }
}

