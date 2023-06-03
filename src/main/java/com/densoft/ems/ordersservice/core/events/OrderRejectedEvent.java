package com.densoft.ems.ordersservice.core.events;

import com.densoft.ems.ordersservice.core.models.OrderStatus;
import lombok.Value;

@Value
public class OrderRejectedEvent {
    private final String orderId;
    private final String reason;
    private final OrderStatus orderStatus = OrderStatus.REJECTED;
}
