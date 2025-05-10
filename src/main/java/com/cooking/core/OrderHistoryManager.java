package com.cooking.core;

import com.cooking.model.Order;
import java.util.*;
import java.util.logging.Logger;

public class OrderHistoryManager {
    private static final Logger logger = Logger.getLogger(OrderHistoryManager.class.getName());
    private final Map<String, List<Order>> orderHistory = new HashMap<>();

    public void addOrder(String customerId, Order order) {
        orderHistory.computeIfAbsent(customerId, k -> new ArrayList<>()).add(order);
        logger.info(() -> String.format(
            "Added order %s for customer %s",
            order.getOrderId(), customerId
        ));
    }

    public List<Order> getOrderHistory(String customerId, UserRole requesterRole, String requesterId) 
        throws SecurityException {
        
        if (!hasAccess(customerId, requesterRole, requesterId)) {
            throw new SecurityException("Access denied");
        }
        return Collections.unmodifiableList(
            orderHistory.getOrDefault(customerId, Collections.emptyList())
        );
    }

    private boolean hasAccess(String customerId, UserRole requesterRole, String requesterId) {
        switch (requesterRole) {
            case CUSTOMER:
                return requesterId.equals(customerId);
            case CHEF:
            case ADMIN:
                return true;
            case UNAUTHORIZED:
            default:
                return false;
        }
    }
}