package com.pm.orderservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {

    @Valid
    @NotNull(message = "Receiver details are required")
    private RecieverDetailsRequest recieverDetailsRequest;
    @Valid
    @NotNull(message = "Receiver address are required")
    private RecieverAddressRequest recieverAddressRequest;
    @Valid
    @NotNull(message = "Product details are required")
    private ProductDetailsRequest productDetailsRequest;
}
