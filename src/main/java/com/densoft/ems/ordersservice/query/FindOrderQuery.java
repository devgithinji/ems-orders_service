package com.densoft.ems.ordersservice.query;

import lombok.Value;

@Value
public class FindOrderQuery {
    private final String orderId;
}
