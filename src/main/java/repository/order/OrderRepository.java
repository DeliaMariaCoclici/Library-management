package repository.order;

import model.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository {
    boolean save(Order order);
    List<Order> findAll();
    List<Order> findBetween(LocalDateTime start, LocalDateTime end);
}
