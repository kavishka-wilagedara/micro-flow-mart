package com.pm.orderservice.service.impl;

import com.pm.orderservice.dto.events.OrderEvent;
import com.pm.orderservice.dto.request.OrderRequest;
import com.pm.orderservice.dto.request.RecieverAddressRequest;
import com.pm.orderservice.dto.request.RecieverDetailsRequest;
import com.pm.orderservice.dto.response.OrderResponse;
import com.pm.orderservice.dto.response.ProductOrderResponse;
import com.pm.orderservice.dto.response.RecieverAddressResponse;
import com.pm.orderservice.dto.response.RecieverDetailsResponse;
import com.pm.orderservice.enums.EventStatus;
import com.pm.orderservice.exception.CustomBusinessException;
import com.pm.orderservice.exception.NotFoundException;
import com.pm.orderservice.kafka.OrderProducer;
import com.pm.orderservice.mappers.OrderMapper;
import com.pm.orderservice.model.Order;
import com.pm.orderservice.enums.OrderStatus;
import com.pm.orderservice.model.RecieverAddress;
import com.pm.orderservice.model.RecieverDetails;
import com.pm.orderservice.repo.OrderRepository;
import com.pm.orderservice.service.OrderService;
import com.pm.orderservice.util.ApplicationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final WebClient productWebClient;
    private final OrderMapper orderMapper;
    private final OrderProducer orderProducer;

    public OrderServiceImpl(OrderRepository orderRepository, WebClient productWebClient,
                            OrderMapper orderMapper, OrderProducer orderProducer) {
        this.orderRepository = orderRepository;
        this.productWebClient = productWebClient;
        this.orderMapper = orderMapper;
        this.orderProducer = orderProducer;
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {

        long productId = orderRequest.getProductDetailsRequest().getProductId();
        int orderQuantity = orderRequest.getProductDetailsRequest().getQuantity();

        ProductOrderResponse productOrderResponse = fetchProductOrderDetails(productId);
        // Validate product availability
        checkProductAvailability(productOrderResponse, orderRequest);

        try{
            // Reduce product quantity for the order
            reserveProductQuantity(productId, orderQuantity);
            log.info("Reserved product stock at order creation productId={} quantity={}" , productId, orderQuantity);
        }
        catch(Exception ex){
            throw new CustomBusinessException("Failed to reserve product stock");
        }

        try {
            OrderResponse orderResponse = buildOrderResponse(orderRequest, productOrderResponse);
            Order orderEntity = buildOrderEntity(orderRequest, orderResponse, productId);
            orderRepository.save(orderEntity);

            // Set Order Status
            orderResponse.setOrderStatus(orderEntity.getStatus());
            // Set orderID to orderResponse
            orderResponse.setOrderId(orderEntity.getId());
            // Send event
            sendEventToKafka(orderResponse, orderEntity);

            return orderResponse;
        }
        catch (Exception ex) {
            // rollback product quantity reduce method
            rollbackProductQuantity(productId, orderQuantity);
            log.info("Rollback product stock at order creation productId={} quantity={}" , productId, orderQuantity);
            throw new CustomBusinessException("Order creation failed");
        }
    }

    @Override
    public OrderResponse updateOrder(OrderRequest updateOrderRequest, long orderId) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ApplicationConstants.ORDER_NOT_FOUND_MESSAGE + orderId));

        if(existingOrder.getStatus() != OrderStatus.PENDING){
            throw new CustomBusinessException("Can't update order, Order already placed to currier service");
        }

        else{
            long productId = updateOrderRequest.getProductDetailsRequest().getProductId();
            int orderQuantity = updateOrderRequest.getProductDetailsRequest().getQuantity();

            ProductOrderResponse productOrderResponse = fetchProductOrderDetails(productId);
            checkProductAvailability(productOrderResponse, updateOrderRequest);

            try{
                // Reduce product quantity for the order
                reserveProductQuantity(productId, orderQuantity);
                log.info("Reserved product stock at order update productId={} quantity={}" , productId, orderQuantity);
            }
            catch(Exception ex){
                throw new CustomBusinessException("Failed to reserve product stock");
            }

            try{
                OrderResponse updateOrderResponse = buildOrderResponse(updateOrderRequest, productOrderResponse);
                Order updatedOrder = updateOrderFields(existingOrder, updateOrderRequest, updateOrderResponse);
                orderRepository.save(updatedOrder);

                updateOrderResponse.setOrderStatus(updatedOrder.getStatus());

                log.info("Order updated successfully");
                return updateOrderResponse;
            }
            catch (Exception ex) {
                // rollback product quantity reduce method
                rollbackProductQuantity(productId, orderQuantity);
                log.info("Rollback product stock at order update productId={} quantity={}" , productId, orderQuantity);
                throw new CustomBusinessException("Order creation failed");
            }
        }
    }

    @Override
    public void deleteOrder(long orderId) {

        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ApplicationConstants.ORDER_NOT_FOUND_MESSAGE + orderId));

        if(existingOrder.getStatus()!= OrderStatus.PENDING){
            throw new CustomBusinessException("Can't delete order, Order already placed to currier service");
        }
        else{
            orderRepository.delete(existingOrder);
        }

    }

    @Override
    public OrderResponse getOrder(long orderId) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ApplicationConstants.ORDER_NOT_FOUND_MESSAGE + orderId));

        return buildOrderResponse(existingOrder);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::buildOrderResponse)
                .toList();
    }

    private void checkProductAvailability(ProductOrderResponse productOrderResponse, OrderRequest orderRequest) {

        LocalDate today = LocalDate.now();
        int availableQuantity = productOrderResponse.getQuantity();
        int orderQuantity = orderRequest.getProductDetailsRequest().getQuantity();

        if (!productOrderResponse.isForSale()){
            throw new CustomBusinessException("Product is not for sale");
        }

        if (availableQuantity == 0){
            throw new CustomBusinessException("Product out of stock");
        }

        if(productOrderResponse.getExpiryDate().isBefore(today)){
            throw new CustomBusinessException("Product has expired");
        }

        if(orderQuantity > availableQuantity){
            throw new CustomBusinessException("Order limit exceed available product limit, Maximum limit: " + availableQuantity);
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

        log.info("Reciever details: {}", recieverDetails);

        // Map reciever address
        RecieverAddress recieverAddress = new RecieverAddress();
        recieverAddress.setPostalCode(addressRequest.getPostalCode());
        recieverAddress.setAddressLine1(addressRequest.getAddressLine1());
        recieverAddress.setAddressLine2(addressRequest.getAddressLine2());
        recieverAddress.setCity(addressRequest.getCity());

        recieverDetails.setAddress(recieverAddress);

        log.info("Reciever address: {}", recieverAddress);
        return recieverDetails;

    }

    private ProductOrderResponse fetchProductOrderDetails(long productId) {

        try{
            return productWebClient.get()
                    .uri("/api/v1/products/get/{productId}", productId)
                    .retrieve()
                    .bodyToMono(ProductOrderResponse.class)
                    .blockOptional()
                    .orElseThrow(() -> new CustomBusinessException("Empty product order response"));
        }
        catch (WebClientRequestException ex){
            log.error("Error fetching product details: {}", ex.getMessage());
            throw new CustomBusinessException("Product service is unreachable or down");
        }
        catch (Exception ex) {
            log.error("Unexpected error fetching product: {}", ex.getMessage());
            throw new CustomBusinessException("Unexpected error while communicating with Product Service");
        }
    }

    private OrderResponse buildOrderResponse(OrderRequest orderRequest, ProductOrderResponse productOrderResponse){

        OrderResponse response = new OrderResponse();
        calculateTotalAmountOfOrder(orderRequest, productOrderResponse, response);
        setOrderDetails(orderRequest, productOrderResponse, response);

        // Set reciever Details and Address
        response.setRecieverDetailsResponse(buildRecieverDetailsResponse(orderRequest));
        response.setRecieverAddressResponse(buildRecieverAddressResponse(orderRequest));
        return response;

    }

    private RecieverDetailsResponse buildRecieverDetailsResponse(OrderRequest orderRequest){
        RecieverDetailsRequest detailsRequest = orderRequest.getRecieverDetailsRequest();
        return RecieverDetailsResponse.builder()
                .name(detailsRequest.getName())
                .phone(detailsRequest.getPhone())
                .email(detailsRequest.getEmail())
                .build();
    }

    private RecieverAddressResponse buildRecieverAddressResponse(OrderRequest orderRequest){
        RecieverAddressRequest addressRequest = orderRequest.getRecieverAddressRequest();
        return RecieverAddressResponse.builder()
                .postalCode(addressRequest.getPostalCode())
                .addressLine1(addressRequest.getAddressLine1())
                .addressLine2(addressRequest.getAddressLine2())
                .city(addressRequest.getCity())
                .build();
    }

    private RecieverDetailsResponse buildRecieverDetailsResponse(Order order){
        RecieverDetails recieverDetails = order.getRecieverDetails();
        return RecieverDetailsResponse.builder()
                .name(recieverDetails.getName())
                .phone(recieverDetails.getPhone())
                .email(recieverDetails.getEmail())
                .build();
    }

    private RecieverAddressResponse buildRecieverAddressResponse(Order order){
        RecieverAddress recieverAddress = order.getRecieverDetails().getAddress();
        return RecieverAddressResponse.builder()
                .postalCode(recieverAddress.getPostalCode())
                .addressLine1(recieverAddress.getAddressLine1())
                .addressLine2(recieverAddress.getAddressLine2())
                .city(recieverAddress.getCity())
                .build();
    }

    private Order buildOrderEntity(OrderRequest orderRequest, OrderResponse orderResponse, long productId){
        return Order.builder()
                .productId(productId)
                .quantity(orderResponse.getQuantity())
                .totalPrice(orderResponse.getTotalPrice())
                .orderDate(orderResponse.getOrderDate())
                .status(OrderStatus.PENDING)
                .recieverDetails(mapRecieverDetailsAndAddress(orderRequest))
                .build();
    }

    private void reserveProductQuantity(long productId, int quantity){
        productWebClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/products/quantity/{productId}/reduce")
                        .queryParam("quantity", quantity)
                        .build(productId))
                .retrieve()
                .bodyToMono(ProductOrderResponse.class)
                .block();
    }

    private void rollbackProductQuantity(long productId, int quantity){
        productWebClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/products/quantity/{productId}/rollback")
                        .queryParam("quantity", quantity)
                        .build(productId))
                .retrieve()
                .bodyToMono(ProductOrderResponse.class)
                .block();
    }

    private OrderResponse buildOrderResponse(Order order){

        long productId = order.getProductId();
        ProductOrderResponse productOrderResponse = fetchProductOrderDetails(productId);

        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        orderResponse.setOrderId(order.getId());
        orderResponse.setProductOrderResponse(productOrderResponse);
        orderResponse.setRecieverDetailsResponse(buildRecieverDetailsResponse(order));
        orderResponse.setRecieverAddressResponse(buildRecieverAddressResponse(order));
        orderResponse.setOrderStatus(order.getStatus());

        return orderResponse;
    }

    private Order updateOrderFields(Order existingOrder, OrderRequest orderRequest, OrderResponse orderResponse){

        long newProductId = orderRequest.getProductDetailsRequest().getProductId();
        long previousProductId = existingOrder.getProductId();

        if(previousProductId != newProductId){
            log.error("Product id does not match previousId={}, newId={}", previousProductId, newProductId);
            throw new CustomBusinessException("Can't update product details");
        }
        existingOrder.setQuantity(orderResponse.getQuantity());
        existingOrder.setTotalPrice(orderResponse.getTotalPrice());
        existingOrder.setStatus(OrderStatus.PENDING);
        existingOrder.setRecieverDetails(mapRecieverDetailsAndAddress(orderRequest));

        return existingOrder;
    }

    private void sendEventToKafka(OrderResponse orderResponse, Order order){

        ProductOrderResponse productOrderResponse = orderResponse.getProductOrderResponse();

        OrderEvent orderEvent = new OrderEvent(
                order.getId(),
                productOrderResponse.getId(),
                productOrderResponse.getName(),
                EventStatus.ORDER_CREATED
        );

        orderProducer.sendOrderEvent(orderEvent);
    }
}
