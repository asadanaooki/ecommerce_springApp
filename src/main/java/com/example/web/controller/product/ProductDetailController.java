package com.example.web.controller.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.model.dto.ProductDetailDto;
import com.example.service.product.ProductDetailService;

import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
@RequestMapping("/product")
@Validated
public class ProductDetailController {

    private final ProductDetailService productDetailService;

    @GetMapping("/{productId}")
    public String showProductDetail(
            @PathVariable @NotNull @Size(max = 64) @Pattern(regexp = "^[0-9A-Fa-f]+$") String productId,
            Model model) {
        ProductDetailDto dto = productDetailService.getProductDetail(productId);
        model.addAttribute("product", dto);

        return "product/ProductDetail";
    }
    
    @PostMapping("/{productId}/favorite/add")
    public ResponseEntity<Void> addFavorite(@PathVariable("productId") String productId){
        productDetailService.addFavorite(productId);
        
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{productId}/favorite/remove")
    public ResponseEntity<Void> removeFavorite(@PathVariable("productId") String productId){
        productDetailService.removeFavorite(productId);
        
        return ResponseEntity.ok().build();
    }
}
