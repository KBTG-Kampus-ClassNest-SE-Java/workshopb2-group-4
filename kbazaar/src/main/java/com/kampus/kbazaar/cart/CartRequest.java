package com.kampus.kbazaar.cart;

import java.math.BigDecimal;

public record CartRequest(String sku, String name, BigDecimal price, Integer quantity, String promotionCodes, BigDecimal discount) {
}
