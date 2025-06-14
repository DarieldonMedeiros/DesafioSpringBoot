package com.zipdin.avaliacao.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ReleaseEntity saveRelease(ReleaseEntity releaseEntity){
        return releaseRepository.save(releaseEntity);
    }

    public Optional<ReleaseEntity> findById(Long id) {
        if (!releaseRepository.existsById(id)) {
            throw new RuntimeException("Release não encontrado com o id: " + id);
        }
        return releaseRepository.findById(id);
    }

    public void updateReleaseNotes(Long id, UpdateNotesDTO updateNotesDTO) {
        Optional<ReleaseEntity> releaseEntityOptional = findById(id);

        // String userUpdate = SecurityContextHolder.getContext().getAuthentication().getName();
        releaseEntityOptional.ifPresent(releaseEntity -> {
            releaseEntity.setNotes(updateNotesDTO.notes());
            // releaseEntity.setUpdatedBy(userUpdate);
            releaseRepository.save(releaseEntity);
        });
    }

    public void deleteRelease(Long id) {
        if (!releaseRepository.existsById(id)) {
            throw new RuntimeException("Release não encontrado com o id: " + id);
        }
        releaseRepository.deleteById(id);
    }

    public Page<ReleaseEntity> findAll(Pageable pageable) {
        return releaseRepository.findAll(pageable);
    }
}
