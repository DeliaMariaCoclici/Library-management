package view.model;

import java.time.LocalDate;
import java.util.List;

public record ReportDTO (long booksSold, double totalValue, List<Line> lines) {
        public record Line(String bookTitle, int quantity, double unitPrice,
                           double totalLine, LocalDate date, String employee) {}
}

