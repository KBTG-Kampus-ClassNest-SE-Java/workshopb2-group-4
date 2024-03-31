package com.kampus.kbazaar.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<__CartItem, Long> {
    List<__CartItem> findAllByUsername(String username);
}
