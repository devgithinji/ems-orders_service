package com.densoft.ordersservice.rest;

import com.densoft.ordersservice.comand.commands.CreateOrderCommand;
import com.densoft.ordersservice.core.models.OrderStatus;
import com.densoft.ordersservice.core.models.OrderSummary;
import com.densoft.ordersservice.query.FindOrderQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersCommandController {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @PostMapping
    public OrderSummary createOrder(@Valid @RequestBody OrderCreateRest order) {
        String userId = "27b95829-4f3f-4ddf-8983-151ba010e35b";
        String orderId = UUID.randomUUID().toString();

        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .addressId(order.getAddressId())
                .productId(order.getProductId())
                .userId(userId)
                .quantity(order.getQuantity())
                .orderId(orderId)
                .orderStatus(OrderStatus.CREATED)
                .build();

        try (SubscriptionQueryResult<OrderSummary, OrderSummary> queryResult = queryGateway.subscriptionQuery(
                new FindOrderQuery(orderId), ResponseTypes.instanceOf(OrderSummary.class), ResponseTypes.instanceOf(OrderSummary.class)
        )) {
            commandGateway.sendAndWait(createOrderCommand);
            return queryResult.updates().blockFirst();
        }

    }


}
