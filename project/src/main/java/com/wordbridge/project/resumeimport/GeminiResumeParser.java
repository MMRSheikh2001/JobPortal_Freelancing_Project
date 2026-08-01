package com.wordbridge.project.resumeimport;

import com.wordbridge.project.dto.responsedto.ResumeResponseDTO;

public interface GeminiResumeParser {

    ResumeImportPreviewDTO parseResume(

            String resumeText,

            ResumeResponseDTO currentResume

    );


}
