package com.kampus.kbazaar.cartItem;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {
    private CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItem> getAll(){
        return cartItemRepository.findAll();
    }

    public List<CartItem> findByUsername(String username) {
        return cartItemRepository.findAllByUsername(username);
    }
}
