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
        if(! cart.isPresent()){
            cart = new Cart();
            cart.setUsername(username);
            cart.setDiscount(0);
            cart.setPromotionCodes("");
        }
        CartItem item = new CartItem();
        Shopper shopper = shopperRepository.findByUsername(username);
        //item.setId(shopper.getId());
        item.setUsername(shopper.getUsername());
        item.setSku(request.sku());
        item.setName(request.name());
        item.setPrice(request.price());
        item.setQuantity(request.quantity());
        item.setDiscount(request.discount());
        item.setPromotionsCode(request.promotionCodes());
        CartItem.save(item);

        List<cartItem> listItem = cartItemRepository.findAllByUsername(username);
        BigDecimal discount =0;
        BigDecimal sumTotal=0;
        for(CartItem item: listItem ){
            discount+=item.getDiscount();
            sumTotal=item.getPrice();
        }
        cart.setTotalDiscount(cart.getDiscount()+discount);
        cart.setSubtotal(sumTotal);
        cart.setGrandTotal(sumTotal-cart.getTotalDiscount());
        cartRepository.save(cart);
        return new CartResponse(cart,listItem);
}
