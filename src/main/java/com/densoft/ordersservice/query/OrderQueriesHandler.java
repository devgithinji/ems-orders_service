package com.densoft.ordersservice.query;

import com.densoft.ordersservice.core.data.OrderEntity;
import com.densoft.ordersservice.core.data.OrdersRepository;
import com.densoft.ordersservice.core.models.OrderSummary;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderQueriesHandler {

    private final OrdersRepository ordersRepository;

    @QueryHandler
    public OrderSummary findOrder(FindOrderQuery findOrderQuery) {
        OrderEntity orderEntity = ordersRepository.findByOrderId(findOrderQuery.getOrderId());
        return new OrderSummary(orderEntity.getOrderId(),
                orderEntity.getOrderStatus(), "");
    }
}
