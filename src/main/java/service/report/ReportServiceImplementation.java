package service.report;

import model.Order;
import service.order.OrderService;
import view.model.ReportDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReportServiceImplementation implements ReportService {
    private final OrderService orderService;

    public ReportServiceImplementation(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public ReportDTO buildLastMonthReport() {
        LocalDateTime end   = LocalDateTime.now();
        LocalDateTime start = end.minusDays(30);
        List<Order> orders  = orderService.getOrdersBetween(start, end);

        long booksSold = orders.stream()
                .mapToLong(Order::getQuantity)
                .sum();
        double total   = orders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();

        List<ReportDTO.Line> lines = orders.stream()
                .map(order -> new ReportDTO.Line(
                        order.getBookTitle(),
                        order.getQuantity(),
                        order.getTotalPrice() / order.getQuantity(),
                        order.getTotalPrice(),
                        order.getOrderDate() != null ? order.getOrderDate().toLocalDate() : LocalDate.now(),
                        order.getEmployeeEmail()))
                .toList();
        return new ReportDTO(booksSold, total, lines);
    }
}