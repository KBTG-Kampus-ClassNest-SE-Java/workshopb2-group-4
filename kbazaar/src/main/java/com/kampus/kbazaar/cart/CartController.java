package com.kampus.kbazaar.cart;

import org.springframework.http.HttpStatus;
import com.kampus.kbazaar.promotion.PromotionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CartController {

    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartResponse>> getCart() {
        return new ResponseEntity<>(cartService.getAll(), HttpStatus.OK);
    }

    private CartService cartService;

    @PostMapping("/carts/{username}/promotions")
    public ResponseEntity<Cart> applyPromotionCode(
            @PathVariable String username,
            @RequestBody PromotionRequest promotionRequest
            ) {
        Cart updatedCart = cartService.applyPromotionCode(username, promotionRequest);
        return ResponseEntity.ok(updatedCart);
    }

}
