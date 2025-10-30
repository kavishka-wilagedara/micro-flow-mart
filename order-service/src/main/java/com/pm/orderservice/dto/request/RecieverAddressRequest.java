package com.pm.orderservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RecieverAddressRequest {

    @NotBlank(message = "Postal code is required")
    private String postalCode;
    @NotNull(message = "Address-line-01 is required")
    private String addressLine1;
    @NotNull(message = "Address-line-02 is required")
    private String addressLine2;
    @NotNull(message = "City is required")
    private String city;
}
