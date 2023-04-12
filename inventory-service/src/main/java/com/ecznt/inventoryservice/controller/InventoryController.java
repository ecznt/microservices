package com.ecznt.inventoryservice.controller;


import com.ecznt.inventoryservice.dto.InventoryResponse;
import com.ecznt.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @Value("${server.port}")
    private String port;


    //http://localhost:8282/api/inventory?skuCode=iphone-13&skuCode=iphone13-red

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){
        System.out.println("Request " + port + " portu tarafından karşılandı.");
        return inventoryService.isInStock(skuCode);
    }
}
