package com.wordbridge.project.util;

import com.wordbridge.project.dto.responsedto.ResumeResponseDTO;
import com.wordbridge.project.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PDFResumeService {



    public byte[] generatePdf(ResumeResponseDTO resume){

        return null;
    }
}
