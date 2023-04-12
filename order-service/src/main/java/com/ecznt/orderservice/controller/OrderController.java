package com.ecznt.orderservice.controller;

import com.ecznt.orderservice.dto.OrderRequest;
import com.ecznt.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest) throws IllegalAccessException, URISyntaxException {
        System.out.println(orderRequest);
        orderService.placeOrder(orderRequest);
        return "Order Placed Successfully";
    }
}
