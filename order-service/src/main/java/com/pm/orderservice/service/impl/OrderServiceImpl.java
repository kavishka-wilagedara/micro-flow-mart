package com.pm.orderservice.service.impl;

import com.pm.orderservice.dto.request.OrderRequest;
import com.pm.orderservice.dto.request.RecieverAddressRequest;
import com.pm.orderservice.dto.request.RecieverDetailsRequest;
import com.pm.orderservice.dto.response.OrderResponse;
import com.pm.orderservice.dto.response.ProductOrderResponse;
import com.pm.orderservice.exception.CustomBusinessException;
import com.pm.orderservice.exception.NotFoundException;
import com.pm.orderservice.model.Order;
import com.pm.orderservice.model.RecieverAddress;
import com.pm.orderservice.model.RecieverDetails;
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
import java.time.LocalDateTime;
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
            ProductOrderResponse productResponseFromService = webClient.get()
                    .uri("/api/v1/products/get/{productId}", productId)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                            clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> Mono.error(new NotFoundException("Product not found with ID: " + productId))))
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                            Mono.error(new CustomBusinessException("Product service is currently unavailable")))
                    .bodyToMono(ProductOrderResponse.class)
                    .blockOptional()
                            .orElseThrow(() -> new CustomBusinessException("Empty product order response"));


            // Validate product availability
            checkProductAvailability(productResponseFromService);

            // For return orderResponse
            OrderResponse orderResponse = new OrderResponse();
            calculateTotalAmountOfOrder(orderRequest, productResponseFromService, orderResponse);
            setOrderDetails(orderRequest, productResponseFromService, orderResponse);

            // Convert OrderResponse DTO into Order
            Order orderEntity = new Order();
            orderEntity.setProductId(productId);
            orderEntity.setQuantity(orderResponse.getQuantity());
            orderEntity.setTotalPrice(orderResponse.getTotalPrice());
            orderEntity.setOrderDate(orderResponse.getOrderDate());

            orderEntity.setRecieverDetails(mapRecieverDetailsAndAddress(orderRequest));

            orderRepository.save(orderEntity);

            return orderResponse;

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

    private void checkProductAvailability(ProductOrderResponse productOrderResponse) {

        LocalDate today = LocalDate.now();

        if (!productOrderResponse.isForSale()){
            throw new CustomBusinessException("Product is not for sale");
        }

        if (productOrderResponse.getQuantity() == 0){
            throw new CustomBusinessException("Product out of stock");
        }

        if(productOrderResponse.getExpiryDate().isBefore(today)){
            throw new CustomBusinessException("Product has expired");
        }
    }

    private void calculateTotalAmountOfOrder(OrderRequest orderRequest, ProductOrderResponse productOrderResponse, OrderResponse orderResponse) {

        double productPrize = productOrderResponse.getPrice();
        int orderQuantity = orderRequest.getProductDetailsRequest().getQuantity();

        double totalAmount = productPrize * orderQuantity;

        orderResponse.setTotalPrice(totalAmount);

    }

    private void setOrderDetails(OrderRequest orderRequest, ProductOrderResponse productOrderResponse, OrderResponse orderResponse) {

        LocalDateTime currentDateTime = LocalDateTime.now();
        int orderQuantity = orderRequest.getProductDetailsRequest().getQuantity();

        // Set order date
        orderResponse.setOrderDate(currentDateTime);
        // Set order quantity
        orderResponse.setQuantity(orderQuantity);
        //Set product details
        orderResponse.setProductOrderResponse(productOrderResponse);

    }

    private RecieverDetails mapRecieverDetailsAndAddress(OrderRequest orderRequest) {

        RecieverDetailsRequest detailsRequest = orderRequest.getRecieverDetailsRequest();
        RecieverAddressRequest addressRequest = orderRequest.getRecieverAddressRequest();

        if (detailsRequest == null || addressRequest == null){
            throw new CustomBusinessException("Reciever details or address is missing");
        }

        // Map reciever details
        RecieverDetails recieverDetails = new RecieverDetails();
        recieverDetails.setName(detailsRequest.getName());
        recieverDetails.setPhone(detailsRequest.getPhone());
        recieverDetails.setEmail(detailsRequest.getEmail());

        // Map reciever address
        RecieverAddress recieverAddress = new RecieverAddress();
        recieverAddress.setPostalCode(addressRequest.getPostalCode());
        recieverAddress.setAddressLine1(addressRequest.getAddressLine1());
        recieverAddress.setAddressLine2(addressRequest.getAddressLine2());
        recieverAddress.setCity(addressRequest.getCity());

        recieverDetails.setAddress(recieverAddress);

        return recieverDetails;

    }
}
