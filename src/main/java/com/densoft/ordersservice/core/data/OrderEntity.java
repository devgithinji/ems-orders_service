package com.densoft.ordersservice.core.data;

import com.densoft.ordersservice.core.models.OrderStatus;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "orders")
public class OrderEntity implements Serializable {

    private static final long serialVersionUID = 5313493413859894403L;

    @Id
    @Column(unique = true)
    public String orderId;
    private String productId;
    private String userId;
    private int quantity;
    private String addressId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
