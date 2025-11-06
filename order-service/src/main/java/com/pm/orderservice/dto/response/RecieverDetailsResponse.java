package com.pm.orderservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecieverDetailsResponse {

    private String name;
    private String email;
    private String phone;
}
