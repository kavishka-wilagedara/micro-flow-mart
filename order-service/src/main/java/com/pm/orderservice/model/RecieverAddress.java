package com.pm.orderservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reciever_address")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecieverAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String postalCode;
    @NotNull
    private String addressLine1;
    @NotNull
    private String addressLine2;
    @NotNull
    private String city;
}
