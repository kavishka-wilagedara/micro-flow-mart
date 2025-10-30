package com.pm.orderservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reciever_details")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecieverDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long orderId;
    @NotNull
    private String name;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private RecieverAddress address;

}
