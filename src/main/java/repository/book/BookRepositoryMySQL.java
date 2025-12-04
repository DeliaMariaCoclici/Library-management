package repository.book;

import model.Book;
import model.builder.BookBuilder;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySQL implements BookRepository {
    private final Connection connection;

    public BookRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book;";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                books.add(getBookFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM book WHERE id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(getBookFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean save(Book book) {
        String sql = "INSERT INTO book (author, title, publishedDate, stock, price) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, book.getAuthor());
            preparedStatement.setString(2, book.getTitle());

            if (book.getPublishedDate() != null) {
                preparedStatement.setDate(3, java.sql.Date.valueOf(book.getPublishedDate()));
            } else {
                preparedStatement.setNull(3, Types.DATE);
            }

            if(book.getStock() != null){
                preparedStatement.setInt(4, book.getStock());
            } else {
                preparedStatement.setNull(4, Types.INTEGER);
            }

            if(book.getPrice() != null){
                preparedStatement.setDouble(5, book.getPrice());
            } else {
                preparedStatement.setNull(5, Types.DOUBLE);
            }

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted == 1) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        book.setId(generatedKeys.getLong(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean sell(Book book) {
        String sql = "UPDATE book SET title = ?, author = ?, stock = ?, price = ? WHERE id = ?;";

        if(book.getId() == null) {
            return false;
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());

            if(book.getStock() != null){
                preparedStatement.setInt(3, book.getStock());
            } else {
                preparedStatement.setNull(3, Types.INTEGER);
            }

            if(book.getPrice() != null){
                preparedStatement.setDouble(4, book.getPrice());
            } else {
                preparedStatement.setNull(4, Types.DOUBLE);
            }

            preparedStatement.setLong(5, book.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean delete(Book book) {
        String sql;
        boolean byId = book.getId() != null;

        if (byId) {
            sql = "DELETE FROM book WHERE id = ?;";
        } else {
            sql = "DELETE FROM book WHERE author = ? AND title = ?;";
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (byId) {
                preparedStatement.setLong(1, book.getId());
            } else {
                preparedStatement.setString(1, book.getAuthor());
                preparedStatement.setString(2, book.getTitle());
            }
            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted >= 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void removeAll() {
        String sql = "TRUNCATE TABLE book;";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        java.sql.Date sqlDate = resultSet.getDate("publishedDate");
        LocalDate publishedDate = null;
        if (sqlDate != null) {
            publishedDate = sqlDate.toLocalDate();
        }

        BookBuilder builder = new BookBuilder();
        builder.setId(resultSet.getLong("id"));
        builder.setTitle(resultSet.getString("title"));
        builder.setAuthor(resultSet.getString("author"));
        builder.setPublishedDate(publishedDate);

        //valori care pot fi null
        Integer stock = resultSet.getObject("stock", Integer.class);
        Double price = resultSet.getObject("price", Double.class);
        builder.setStock(stock);
        builder.setPrice(price);

        return builder.build();
    }
}
