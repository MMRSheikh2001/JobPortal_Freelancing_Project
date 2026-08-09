package com.wordbridge.project.report;

import com.wordbridge.project.enums.ReportStatus;
import com.wordbridge.project.enums.ReportType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ReportService {


    //--------------------------------
    // User
    //--------------------------------

    ReportResponseDTO createReport(
            Long userId,
            String subject,
            String description,
            ReportType type,
            MultipartFile attachment
    );

    //--------------------------------
    // Admin
    //--------------------------------

    ReportResponseDTO resolveReport(
            Long reportId,
            String adminReply
    );

    ReportResponseDTO rejectReport(
            Long reportId,
            String adminReply
    );

    //--------------------------------
    // Queries
    //--------------------------------

    List<ReportResponseDTO> getAll();

    ReportResponseDTO getById(
            Long reportId
    );

    List<ReportResponseDTO> getByUserId(
            Long userId
    );

    List<ReportResponseDTO> getByStatus(
            ReportStatus status
    );

    Long countByStatus(
            ReportStatus status
    );

    List<ReportResponseDTO> search(
            ReportFilterRequestDTO dto
    );
}
