package com.kampus.kbazaar.cart;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kampus.kbazaar.security.JwtAuthFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
@WebMvcTest(
        controllers = CartController.class,
        excludeFilters =
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = JwtAuthFilter.class))
public class CartControllerTest {

    @Autowired private MockMvc mockMvc;

    @InjectMocks private CartController cartController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    @DisplayName("should return cart")
    public void getCart_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/carts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should return cart by username")
    public void getCartByUsername_ReturnsOk() throws Exception {
        // Given
        String username = "username";
        // When & Then
        mockMvc.perform(get("/api/v1/carts/" + username).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("post promotion code to cart should return updated cart with discount")
    public void updateDiscount_ReturnsOk() throws Exception {
        // Given
        String username = "username";
        String promotionCode = "code";
        String productSku = "sku";

        // Create a JSON object for the request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("promotionCode", promotionCode);
        requestBody.put("productSku", productSku);

        // Mock the service layer
        Cart updatedCart = new Cart();
        updatedCart.setDiscount(new BigDecimal("10.00"));
        when(cartService.applyPromotion(anyString(), anyString(), anyString())).thenReturn(updatedCart);

        // When & Then
        mockMvc.perform(
                        post("/api/v1/carts/" + username + "/promotion")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discount").value("10.00"));

        // Test without product SKU
        requestBody.remove("productSku");
        when(cartService.applyPromotion(anyString(), anyString(), isNull())).thenReturn(updatedCart);

        mockMvc.perform(
                        post("/api/v1/carts/" + username + "/promotion")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discount").value("10.00"));
    }
}
