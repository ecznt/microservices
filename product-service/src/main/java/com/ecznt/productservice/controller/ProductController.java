package com.ecznt.productservice.controller;

import com.ecznt.productservice.dto.ProductRequest;
import com.ecznt.productservice.dto.ProductResponse;
import com.ecznt.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createProduct(@RequestBody ProductRequest productRequest){
        productService.createProduct(productRequest);
        return "Product " + productRequest.getName()+ " saved.";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        System.out.println("Product Service çalıştı");
        return productService.getAllProducts();
    }


}
