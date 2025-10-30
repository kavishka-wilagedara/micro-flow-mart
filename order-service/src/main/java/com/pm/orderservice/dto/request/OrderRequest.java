package com.pm.orderservice.dto.request;

import lombok.Data;

@Data
public class OrderRequest {

    private RecieverDetailsRequest recieverDetailsRequest;
    private RecieverAddressRequest recieverAddressRequest;
    private ProductDetailsRequest productDetailsRequest;
}
