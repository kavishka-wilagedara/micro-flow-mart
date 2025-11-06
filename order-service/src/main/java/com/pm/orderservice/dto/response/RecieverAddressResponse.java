package com.pm.orderservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecieverAddressResponse {

    private String postalCode;
    private String addressLine1;
    private String addressLine2;
    private String city;
}
