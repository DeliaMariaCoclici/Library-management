package service.report;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import view.model.ReportDTO;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PDFReport {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    
    private String removeDiacritics(String text) {
        return text.replace("ă", "a")
                .replace("Ă", "A")
                .replace("ș", "s")
                .replace("Ș", "S")
                .replace("ț", "t")
                .replace("Ț", "T")
                .replace("â", "a")
                .replace("Â", "A")
                .replace("î", "i")
                .replace("Î", "I");
    }

    public File generate(ReportDTO dto, String fileName) throws IOException {
        File file = new File(fileName);
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                // Titlu 
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.beginText();
                content.newLineAtOffset(100, 700);
                content.showText(removeDiacritics("Raport activitate – ultimele 30 zile"));
                content.endText();

                // Totaluri
                content.setFont(PDType1Font.HELVETICA, 12);
                content.beginText();
                content.newLineAtOffset(100, 670);
                content.showText("Total carti vandute: " + dto.booksSold());
                content.newLineAtOffset(0, -15);
                content.showText(String.format("Valoare totala: %.2f lei", dto.totalValue()));
                content.endText();

                // Header tabel
                int y = 630;
                content.setFont(PDType1Font.HELVETICA_BOLD, 10);
                content.beginText();
                content.newLineAtOffset(100, y);
                content.showText("Titlu");
                content.newLineAtOffset(150, 0);
                content.showText("Cantitate");
                content.newLineAtOffset(50, 0);
                content.showText("Pret/bucata");
                content.newLineAtOffset(60, 0);
                content.showText("Total");
                content.newLineAtOffset(60, 0);
                content.showText("Data");
                content.newLineAtOffset(70, 0);
                content.showText("Employee");
                content.endText();

                // Linii tabel
                y -= 15;
                content.setFont(PDType1Font.HELVETICA, 9);
                for (var line : dto.lines()) {
                    content.beginText();
                    content.newLineAtOffset(100, y);
                    content.showText(removeDiacritics(line.bookTitle()));
                    content.newLineAtOffset(150, 0);
                    content.showText(String.valueOf(line.quantity()));
                    content.newLineAtOffset(50, 0);
                    content.showText(String.format("%.2f", line.unitPrice()));
                    content.newLineAtOffset(60, 0);
                    content.showText(String.format("%.2f", line.totalLine()));
                    content.newLineAtOffset(60, 0);
                    content.showText(line.date().format(FMT));
                    content.newLineAtOffset(70, 0);
                    content.showText(removeDiacritics(line.employee()));
                    content.endText();
                    y -= 12;

                    // Adauga pagina - marginea paginii
                    if (y < 50) {
                        content.close();
                        PDPage newPage = new PDPage();
                        doc.addPage(newPage);
                        y = 700;
                    }
                }
            }

            doc.save(file);
        }
        return file;
    }
}
