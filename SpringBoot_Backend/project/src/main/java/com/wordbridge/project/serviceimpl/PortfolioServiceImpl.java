package com.wordbridge.project.serviceimpl;

import com.wordbridge.project.dto.mapper.PortfolioMapper;
import com.wordbridge.project.dto.requestdto.PortfolioRequestDTO;
import com.wordbridge.project.dto.responsedto.PortfolioResponseDTO;
import com.wordbridge.project.entity.Portfolio;
import com.wordbridge.project.repository.PortfolioRepository;
import com.wordbridge.project.service.PortfolioService;
import com.wordbridge.project.util.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final PortfolioMapper portfolioMapper;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public PortfolioResponseDTO save(PortfolioRequestDTO dto, MultipartFile file) {
        Portfolio portfolio = portfolioMapper.toEntity(dto);

        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageService.uploadPortfolioFile(file,
                    portfolio.getUserProfile().getUser().getEmail(),
                    "portfolios");
            portfolio.setFileUrl(fileName);

        }

        return portfolioMapper.toDTO(portfolioRepository.save(portfolio));
    }

    @Override
    public List<PortfolioResponseDTO> getAll() {
        return portfolioRepository.findAll().stream().map(portfolioMapper::toDTO).toList();
    }

    @Override
    public PortfolioResponseDTO findById(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Portfolio Found by this id"));
        return portfolioMapper.toDTO(portfolio);
    }

    @Override
    @Transactional
    public PortfolioResponseDTO update(Long id, PortfolioRequestDTO dto, MultipartFile file) {

        Portfolio exist = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Portfolio Found by this id"));

        Portfolio portfolio = portfolioMapper.toEntity(dto);

        if (file != null && !file.isEmpty()) {


            String fileName = fileStorageService.uploadPortfolioFile(file,
                    portfolio.getUserProfile().getUser().getEmail(),
                    "portfolios");
            portfolio.setFileUrl(fileName);

            if (exist.getFileUrl() != null) {
                fileStorageService.deleteFile(
                        "portfolios",
                        exist.getFileUrl()
                );
            }

        } else {
            portfolio.setFileUrl(exist.getFileUrl());
        }
        portfolio.setId(exist.getId());
        portfolio.setCreatedAt(exist.getCreatedAt());

        return portfolioMapper.toDTO(portfolioRepository.save(portfolio));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Portfolio found"));

        if (portfolio.getFileUrl() != null) {
            fileStorageService.deleteFile(
                    "portfolios",
                    portfolio.getFileUrl()
            );
        }

        portfolioRepository.delete(portfolio);

    }

    @Override
    @Transactional
    public void deleteFile(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio Not Found"));

        if (portfolio.getFileUrl() != null) {

            fileStorageService.deleteFile("portfolios",
                    portfolio.getFileUrl()

            );

            portfolio.setFileUrl(null);

            portfolioRepository.save(portfolio);
        }


    }

    @Override
    public List<PortfolioResponseDTO> findByUserProfileId(Long userProfileId) {
        return portfolioRepository.findByUserProfileId(userProfileId).stream().map(portfolioMapper::toDTO).toList();
    }

    @Override
    public Long countByUserProfileId(Long userProfileId) {
        return portfolioRepository.countByUserProfileId(userProfileId);
    }
}
