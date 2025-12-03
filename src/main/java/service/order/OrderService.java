package service.order;

import model.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    boolean saveOrder(Order order);
    List<Order> getAllOrders();
    List<Order> getOrdersBetween(LocalDateTime start, LocalDateTime end);
}