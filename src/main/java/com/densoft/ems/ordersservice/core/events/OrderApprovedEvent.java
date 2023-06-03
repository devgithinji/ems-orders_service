package com.densoft.ems.ordersservice.core.events;

import com.densoft.ems.ordersservice.core.models.OrderStatus;
import lombok.Value;

@Value
public class OrderApprovedEvent {
    private final String orderId;
    private final OrderStatus orderStatus = OrderStatus.APPROVED;
}
