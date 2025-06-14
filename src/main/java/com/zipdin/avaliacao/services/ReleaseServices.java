package com.zipdin.avaliacao.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zipdin.avaliacao.dto.UpdateNotesDTO;
import com.zipdin.avaliacao.entities.ReleaseEntity;
import com.zipdin.avaliacao.repository.ReleaseRepository;

import jakarta.transaction.Transactional;

@Service
public class ReleaseServices {

    
    final ReleaseRepository releaseRepository;

    public ReleaseServices(ReleaseRepository releaseRepository) {
        this.releaseRepository = releaseRepository;
    }

    @Transactional
    public ReleaseEntity save(ReleaseEntity releaseEntity){
        return releaseRepository.save(releaseEntity);
    }

    public Optional<ReleaseEntity> findById(Long id) {
        return releaseRepository.findById(id);
    }

    public void updateReleaseNotes(Long id, UpdateNotesDTO updateNotesDTO) {
        ReleaseEntity releaseEntity = findReleaseById(id);

        // String userUpdate = SecurityContextHolder.getContext().getAuthentication().getName();
        releaseEntity.setNotes(updateNotesDTO.notes());
        // releaseEntity.setUpdatedBy(userUpdate);
        
        releaseRepository.save(releaseEntity);
    }

    private ReleaseEntity findReleaseById(Long id) {
        return releaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Release não encontrado com ID: " + id));
    }
}
