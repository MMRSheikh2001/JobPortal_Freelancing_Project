package com.wordbridge.project.saved;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SavedGigService {

    SavedGigResponseDTO saveGig(Long userId, Long gigId);

    void unsaveGig(Long userId, Long gigId);

    List<SavedGigResponseDTO> getSavedGigs(Long userId);

    boolean isGigSaved(Long userId, Long gigId);

    Long countByUserId(Long userId);


}
