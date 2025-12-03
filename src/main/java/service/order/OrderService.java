package service.order;

import model.Order;
import java.util.List;

public interface OrderService {
    boolean saveOrder(Order order);
    List<Order> getAllOrders();
}