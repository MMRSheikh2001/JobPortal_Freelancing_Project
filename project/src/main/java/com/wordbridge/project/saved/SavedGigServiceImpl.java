package com.wordbridge.project.saved;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.gig.Gig;
import com.wordbridge.project.gig.GigRepository;
import com.wordbridge.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedGigServiceImpl implements SavedGigService {
    private final SavedGigRepository savedGigRepository;
    private final SavedGigMapper savedGigMapper;
    private final UserRepository userRepository;
    private final GigRepository gigRepository;


    @Override
    public SavedGigResponseDTO saveGig(Long userId, Long gigId) {
        if (savedGigRepository.existsByUserIdAndGigId(userId, gigId)) {
            throw new RuntimeException("Gig is already saved");
        }
        SavedGig savedGig = new SavedGig();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No user found"));
        Gig gig = gigRepository.findById(gigId)
                .orElseThrow(() -> new RuntimeException("No Gig found"));
        savedGig.setUser(user);
        savedGig.setGig(gig);

        return savedGigMapper.toDTO(savedGigRepository.save(savedGig));
    }

    @Override
    public void unsaveGig(Long userId, Long gigId) {
        if (!savedGigRepository.existsByUserIdAndGigId(userId, gigId)) {
            throw new RuntimeException("Gig is not saved");
        }
        SavedGig savedGig = savedGigRepository.findByUserIdAndGigId(userId, gigId)
                .orElseThrow(() -> new RuntimeException("No Saved Gig found"));
        savedGigRepository.delete(savedGig);
    }

    @Override
    public List<SavedGigResponseDTO> getSavedGigs(Long userId) {
        return savedGigRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(savedGigMapper::toDTO).toList();

    }

    @Override
    public boolean isGigSaved(Long userId, Long gigId) {
        return savedGigRepository.existsByUserIdAndGigId(userId, gigId);
    }

    @Override
    public Long countByUserId(Long userId) {
        return savedGigRepository.countByUserId(userId);
    }
}
