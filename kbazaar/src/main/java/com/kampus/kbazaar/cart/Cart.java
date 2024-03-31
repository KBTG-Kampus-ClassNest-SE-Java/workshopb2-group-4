package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.cartItem.CartItem;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

import jdk.jfr.Description;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "cart")
@Setter
@Getter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "total_discount")
    private BigDecimal totalDiscount;

    @Column(name = "promotion_codes")
    private String promotionCodes;

    @Description("precisely reflect its pre-discount status")
    @Column(name = "subtotal")
    private BigDecimal subtotal;

    @Description("the final, all-inclusive amount to be paid.")
    @Column(name = "grand_total")
    private BigDecimal grandTotal;

    public String tryPush;

    public CartResponse toResponse(List<CartItem> listItem){
        return new CartResponse(this.username, listItem,this.discount,this.totalDiscount, this.subtotal, this.grandTotal,this.promotionCodes);}
}
