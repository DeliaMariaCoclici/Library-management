package repository.order;

import model.Order;

import java.util.List;

public interface OrderRepository {
    boolean save(Order order);
    List<Order> findAll();
}
