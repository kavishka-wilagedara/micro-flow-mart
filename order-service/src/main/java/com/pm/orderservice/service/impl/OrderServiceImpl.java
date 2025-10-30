package com.pm.orderservice.service.impl;

import com.pm.orderservice.dto.request.OrderRequest;
import com.pm.orderservice.dto.request.ProductDetailsRequest;
import com.pm.orderservice.dto.response.OrderResponse;
import com.pm.orderservice.dto.response.ProductOrderResponse;
import com.pm.orderservice.dto.response.ProductResponse;
import com.pm.orderservice.exception.CustomBusinessException;
import com.pm.orderservice.exception.NotFoundException;
import com.pm.orderservice.repo.OrderRepository;
import com.pm.orderservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public OrderServiceImpl(OrderRepository orderRepository, WebClient webClient) {
        this.orderRepository = orderRepository;
        this.webClient = webClient;
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {

        long productId = orderRequest.getProductDetailsRequest().getProductId();

        try{
            ProductResponse productResponseFromService = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("http://localhost:8081/api/v1/products/get/{productId}").build(productId))
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                            clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> Mono.error(new NotFoundException("Product not found with ID: " + productId))))
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                            Mono.error(new CustomBusinessException("Product service is currently unavailable")))
                    .bodyToMono(ProductResponse.class)
                    .block();

            // Check product's business logics
            checkProductAvailability(productResponseFromService);



        }
        catch (NotFoundException ex){
            log.error("Error: {}", ex.getMessage());
            throw new CustomBusinessException("Can't create order: Product not found");
        }
        catch (WebClientResponseException ex){
            log.error("Error: {}", ex.getMessage());
            throw new CustomBusinessException("Product service is currently unavailable");
        }
        catch (Exception ex){
            log.error("Error: {}", ex.getMessage());
            throw new CustomBusinessException("Unexpected error");
        }

        return null;
    }

    @Override
    public OrderResponse updateOrder(OrderRequest orderRequest) {
        return null;
    }

    @Override
    public void deleteOrder(long orderId) {

    }

    @Override
    public OrderResponse getOrder(long orderId) {
        return null;
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return List.of();
    }

    private void checkProductAvailability(ProductResponse productResponse) {

        LocalDate today = LocalDate.now();

        if (!productResponse.isForSale()){
            throw new CustomBusinessException("Product is not for sale");
        }

        if (productResponse.getQuantity() == 0){
            throw new CustomBusinessException("Product out of stock");
        }

        if(productResponse.getExpiryDate().isBefore(today)){
            throw new CustomBusinessException("Product has expired");
        }

    }
}
