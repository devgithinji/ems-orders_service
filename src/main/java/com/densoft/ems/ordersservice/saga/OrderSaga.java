package com.densoft.ems.ordersservice.saga;

import com.densoft.ems.common.commands.ReserveProductCommand;
import com.densoft.ems.common.events.ProductReservedEvent;
import com.densoft.ems.common.model.User;
import com.densoft.ems.common.query.FetchUserPaymentDetailsQuery;
import com.densoft.ems.ordersservice.core.events.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;


@Saga
@RequiredArgsConstructor
@Slf4j
public class OrderSaga {
    private transient CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
                .orderId(orderCreatedEvent.getOrderId())
                .productId(orderCreatedEvent.getProductId())
                .quantity(orderCreatedEvent.getQuantity())
                .userId(orderCreatedEvent.getUserId()).
                build();

        log.info("order created event for orderId: " + reserveProductCommand.getOrderId() + " and productId: " + reserveProductCommand.getProductId());

        commandGateway.send(reserveProductCommand, (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                //start a compensating transaction
            }
        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent productReservedEvent) {
        //process user payment
        log.info("ProductReserved is called for productId: " + productReservedEvent.getProductId() + " and orderId: " + productReservedEvent.getOrderId());
        User userPaymentDetails = null;

        try {
            FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery = new FetchUserPaymentDetailsQuery(productReservedEvent.getUserId());
            userPaymentDetails = queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
        } catch (Exception e) {
            log.error(e.getMessage());
            //start compensating transaction
            return;
        }

        if(userPaymentDetails == null){
            //start compensating transaction
            return;
        }

        log.info("Successfully fetched user payment details for user: " + userPaymentDetails.getFirstName());
    }

}
