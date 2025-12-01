package repository.order;

import model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryMySQL implements OrderRepository {
    private final Connection connection;

    public OrderRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean save(Order order) {
        String sql = "INSERT INTO orders (book_title, quantity, employee_email, total_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, order.getBookTitle());
            ps.setInt(2, order.getQuantity());
            ps.setString(3, order.getEmployeeEmail());
            ps.setDouble(4, order.getTotalPrice());
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
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }
}