package repository.order;

import model.Order;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryMySQL implements OrderRepository {
    private final Connection connection;

    public OrderRepositoryMySQL(Connection connection) { this.connection = connection; }

    @Override
    public boolean save(Order order) {
        String sql = "INSERT INTO orders (book_title, quantity, employee_email, total_price, order_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, order.getBookTitle());
            ps.setInt(2, order.getQuantity());
            ps.setString(3, order.getEmployeeEmail());
            ps.setDouble(4, order.getTotalPrice());
            ps.setTimestamp(5, Timestamp.valueOf(
                    order.getOrderDate() != null ? order.getOrderDate() : LocalDateTime.now()));
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getLong("id"));
                order.setBookTitle(rs.getString("book_title"));
                order.setQuantity(rs.getInt("quantity"));
                order.setEmployeeEmail(rs.getString("employee_email"));
                order.setTotalPrice(rs.getDouble("total_price"));
                Timestamp ts = rs.getTimestamp("order_date");
                order.setOrderDate(ts != null ? ts.toLocalDateTime() : LocalDateTime.now());

                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<Order> findBetween(LocalDateTime start, LocalDateTime end) {
        String sql = "SELECT * FROM orders WHERE order_date BETWEEN ? AND ? ORDER BY order_date DESC";
        List<Order> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(start));
            ps.setTimestamp(2, Timestamp.valueOf(end));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getLong("id"));
                order.setBookTitle(rs.getString("book_title"));
                order.setQuantity(rs.getInt("quantity"));
                order.setEmployeeEmail(rs.getString("employee_email"));
                order.setTotalPrice(rs.getDouble("total_price"));
                Timestamp ts = rs.getTimestamp("order_date");
                order.setOrderDate(ts != null ? ts.toLocalDateTime() : LocalDateTime.now());

                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}