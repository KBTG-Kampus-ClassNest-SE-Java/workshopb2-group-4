package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.cartItem.CartItem;
import com.kampus.kbazaar.cartItem.CartItemService;
import com.kampus.kbazaar.exceptions.NotFoundException;
import com.kampus.kbazaar.promotion.PromotionRequest;
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
    public Cart applyPromotionCode(String username, PromotionRequest promotionRequest) {
        Cart cart = cartRepository.findByUsername(username);

        if (cart == null) {
            return null;
        }

        if (cart.getItems() != null && !cart.getItems().isEmpty()) {
            List<CartItem> items = cart.getItems();
            BigDecimal totalDiscount = BigDecimal.ZERO;

            for (CartItem item : items) {
                if (promotionRequest.getProductSkus().contains(item.getSku())) {
                    BigDecimal discountAmount = promotionRequest.getDiscountAmount();
                    item.setDiscount(discountAmount);
                    totalDiscount = totalDiscount.add(discountAmount);
                    item.setPromotion_codes(promotionRequest.getCode());
                }
            }

            cart.setTotalDiscount(totalDiscount);
            BigDecimal subTotal = calculateSubTotal(cart.getItems());
            cart.setSubtotal(subTotal);
            BigDecimal grandTotal = subTotal.subtract(totalDiscount);
            cart.setGrandTotal(grandTotal);

            cart = cartRepository.save(cart);
        }
        return cart;
    }
    private BigDecimal calculateSubTotal(List<CartItem> items) {
        BigDecimal subTotal = BigDecimal.ZERO;
        for (CartItem item : items) {
            BigDecimal quantity = item.getQuantity();
            BigDecimal itemPrice = item.getPrice();
            BigDecimal itemSubTotal = itemPrice.multiply(quantity);
            subTotal = subTotal.add(itemSubTotal);
        }
        return subTotal;
    }
}
