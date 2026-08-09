package com.wordbridge.project.resumeimport;

public interface ResumeImportService {


    ResumeImportPreviewDTO getPreviewFromGemini(Long userProfileId);

  void   saveImportedResume(Long userProfileId,ResumeImportPreviewDTO preview);


    String extractTextFromCV(Long resumeFileId);


}
