package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.cartItem.CartItem;
import com.kampus.kbazaar.cartItem.CartItemService;
import com.kampus.kbazaar.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CartServiceTest {

    @Mock private CartRepository cartRepository;
    @Mock private CartItemService cartItemService;

    @InjectMocks private CartService cartService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldBeThrowNotFoundExceptionWhenCardEmpty(){
        //Arrange
        when(cartRepository.findAll()).thenReturn(List.of());

        //Act & Assert
        assertThrows(NotFoundException.class,() -> cartService.getAll());

    }

    @Test
    void shouldBeAbleToListCartResponse(){
        //Arrange
        String username = "TechNinja";
        Cart cart = new Cart();
        cart.setUsername(username);
        when(cartRepository.findAll()).thenReturn(List.of(cart));

        CartItem cartItem = new CartItem(1L,"TechNinja","BEV-COCA-COLA","Coca-Cola",BigDecimal.valueOf(10),BigDecimal.valueOf(10),BigDecimal.valueOf(10),"FIXEDAMOUNT10");

        when(cartItemService.findByUsername(username)).thenReturn(List.of(cartItem));

        //Act
        List<CartResponse> actualResult = cartService.getAll();

        //Assert
        assertEquals(username,actualResult.get(0).getUsername());


    }
}
