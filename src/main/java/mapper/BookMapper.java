package mapper;

import model.Book;
import model.builder.BookBuilder;
import view.builder.BookDTOBuilder;
import view.model.BookDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BookMapper {
    public static BookDTO convertBookToDTO(Book book) {
        return new BookDTOBuilder()
                .setId(book.getId())
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .build();
    }

    public static Book convertBookDTOToBook(BookDTO bookDTO) {
        BookBuilder builder = new BookBuilder()
                .setTitle(bookDTO.getTitle())
                .setAuthor(bookDTO.getAuthor());
        if (bookDTO.getId() != null) {
            builder.setId(bookDTO.getId());
        }
        return builder.build();
    }

    public static List<BookDTO> convertBookListToDTOList(List<Book> books) {
        return books.parallelStream().map(BookMapper::convertBookToDTO).collect(Collectors.toList());
    }

    public static List<Book> convertBookDTOListToList(List<BookDTO> bookDTOS) {
        return bookDTOS.parallelStream().map(BookMapper::convertBookDTOToBook).collect(Collectors.toList());
    }

}