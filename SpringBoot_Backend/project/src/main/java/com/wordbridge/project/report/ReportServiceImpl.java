package com.wordbridge.project.report;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.ReportStatus;
import com.wordbridge.project.enums.ReportType;
import com.wordbridge.project.repository.UserRepository;
import com.wordbridge.project.util.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;


    @Override
    @Transactional
    public ReportResponseDTO createReport(Long userId, String subject, String description, ReportType type, MultipartFile attachment) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("No user found"));

        Report report = new Report();
        report.setUser(user);
        report.setType(type);
        report.setSubject(subject);
        report.setDescription(description);
        if (attachment != null && !attachment.isEmpty()) {
            String fileName = fileStorageService.uploadPortfolioFile(attachment,
                    user.getEmail(),
                    "reports");
            report.setAttachmentUrl(fileName);

        }
        report.setStatus(ReportStatus.OPEN);

        Report saved = reportRepository.save(report);

        return reportMapper.toDTO(saved);
    }

    @Override
    public ReportResponseDTO resolveReport(Long reportId, String adminReply) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("No report found"));
        report.setStatus(ReportStatus.RESOLVED);
        report.setAdminReply(adminReply);
        report.setResolvedAt(LocalDateTime.now());


        Report saved = reportRepository.save(report);

        return reportMapper.toDTO(saved);
    }

    @Override
    public ReportResponseDTO rejectReport(Long reportId, String adminReply) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("No report found"));
        report.setStatus(ReportStatus.REJECTED);
        report.setAdminReply(adminReply);


        Report saved = reportRepository.save(report);

        return reportMapper.toDTO(saved);
    }

    @Override
    public List<ReportResponseDTO> getAll() {
        return reportRepository.findAll().stream()
                .map(reportMapper::toDTO).toList();
    }

    @Override
    public ReportResponseDTO getById(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("No report found"));
        return reportMapper.toDTO(report);
    }

    @Override
    public List<ReportResponseDTO> getByUserId(Long userId) {
        return reportRepository.findByUserId(userId).stream()
                .map(reportMapper::toDTO).toList();
    }

    @Override
    public List<ReportResponseDTO> getByStatus(ReportStatus status) {
        return reportRepository.findByStatus(status).stream()
                .map(reportMapper::toDTO).toList();
    }

    @Override
    public Long countByStatus(ReportStatus status) {


        return reportRepository.countByStatus(status);
    }

    @Override
    public List<ReportResponseDTO> search(
            ReportFilterRequestDTO dto
    ) {

        return reportRepository.findAll(
                        ReportSpecification.filter(dto)
                )
                .stream()
                .map(reportMapper::toDTO)
                .toList();

    }
}
