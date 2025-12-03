package service.order;

import model.Order;
import repository.order.OrderRepository;

import java.util.List;

public class OrderServiceImplementation implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImplementation(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}