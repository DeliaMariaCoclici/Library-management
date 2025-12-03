package service.report;

import view.model.ReportDTO;

public interface ReportService {
    ReportDTO buildLastMonthReport();
}