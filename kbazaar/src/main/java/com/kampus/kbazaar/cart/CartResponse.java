package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.cartItem.CartItem;

import java.math.BigDecimal;
import java.util.List;

public class CartResponse {
    private String username;
    private List<CartItem> items;
    private BigDecimal discount;
    private BigDecimal totalDiscount;
    private BigDecimal subtotal;
    private BigDecimal grandTotal;
    private String promotionCodes;

    private BigDecimal shippingFee;


    public CartResponse(String username, List<CartItem> items, BigDecimal discount, BigDecimal totalDiscount, BigDecimal subtotal, BigDecimal grandTotal, String promotionCodes ,
                        BigDecimal shippingFee) {
        this.username = username;
        this.items = items;
        this.discount = discount;
        this.totalDiscount = totalDiscount;
        this.subtotal = subtotal;
        this.grandTotal = grandTotal;
        this.promotionCodes = promotionCodes;
        this.shippingFee = shippingFee;
    }

    public String getPromotionCodes() {
        return promotionCodes;
    }

    public void setPromotionCodes(String promotionCodes) {
        promotionCodes = promotionCodes;
    }

    public CartResponse(String username, List<CartItem> items, BigDecimal discount, BigDecimal totalDiscount, BigDecimal subtotal, BigDecimal grandTotal) {
        this.username = username;
        this.items = items;
        this.discount = discount;
        this.totalDiscount = totalDiscount;
        this.subtotal = subtotal;
        this.grandTotal = grandTotal;
    }

    public String getUsername() {
        return username;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
    }
}
