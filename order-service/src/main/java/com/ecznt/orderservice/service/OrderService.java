package com.ecznt.orderservice.service;

import com.ecznt.orderservice.dto.InventoryResponse;
import com.ecznt.orderservice.dto.OrderLineItemsDto;
import com.ecznt.orderservice.dto.OrderRequest;
import com.ecznt.orderservice.model.Order;
import com.ecznt.orderservice.model.OrderLineItems;
import com.ecznt.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;


    private final WebClient.Builder webClientBuilder;


    private final RestTemplate restTemplate;

    public void placeOrder(OrderRequest orderRequest) throws IllegalAccessException, URISyntaxException {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());


        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(orderLineItemsDto -> mapToDto(orderLineItemsDto))
                .toList();

        order.setOrderLineItemsList(orderLineItems);


        List<String> skuCodes = order.getOrderLineItemsList()
                .stream().map(orderLineItem -> orderLineItem.getSkuCode())
                .toList();
        //Call Inventory Service, and place order if product is in
        //stock
//        InventoryResponse[] inventoryResponsesArray = webClientBuilder.build().get()
//                .uri("http://inventory-service/api/inventory",
//                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
//                .retrieve()
//                .bodyToMono(InventoryResponse[].class)
//                .block();


        UriComponents uriComponentsBuilder = UriComponentsBuilder.newInstance().queryParam("skuCode", skuCodes).build();
        System.out.println("http://inventory-service/api/inventory"+ uriComponentsBuilder );



        InventoryResponse[] inventoryResponsesArray =  restTemplate.getForObject("http://inventory-service/api/inventory" + uriComponentsBuilder, InventoryResponse[] .class );


        boolean allProductsInStock = Arrays.stream(inventoryResponsesArray).allMatch(inventoryResponse -> inventoryResponse.isInStock());

        System.out.println(allProductsInStock);

        if(allProductsInStock){
            orderRepository.save(order);
        }else {
            throw new IllegalAccessException("Product is not in stock please try again later.");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        return orderLineItems;
    }
}
