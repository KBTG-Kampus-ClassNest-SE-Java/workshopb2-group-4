package com.kampus.kbazaar.cart;

import org.springframework.http.HttpStatus;
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
    @PostMapping("/carts/{username}/items")
    public CartReponse addToCart(@PathVariable String username,@RequestBody CartRequest request) {
        return null;
    }
}
