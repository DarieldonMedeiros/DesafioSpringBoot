package com.zipdin.avaliacao.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.zipdin.avaliacao.dto.AddCommitsRequestDTO;
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
        String userUpdate = SecurityContextHolder.getContext().getAuthentication().getName();
        releaseEntity.setUserUpdate(userUpdate);
        return releaseRepository.save(releaseEntity);
    }

    @Transactional
    public Optional<ReleaseEntity> findById(Long id) {
        if (!releaseRepository.existsById(id)) {
            throw new RuntimeException("Release não encontrado com o id: " + id);
        }
        return releaseRepository.findById(id);
    }

    @Transactional
    public void updateReleaseNotes(Long id, UpdateNotesDTO updateNotesDTO) {
        String userUpdate = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<ReleaseEntity> releaseEntityOptional = findById(id);
        releaseEntityOptional.ifPresent(releaseEntity -> {
            releaseEntity.setUserUpdate(userUpdate);
            releaseEntity.setNotes(updateNotesDTO.notes());
            releaseRepository.save(releaseEntity);
        });
    }

    @Transactional
    public void deleteRelease(Long id) {
        if (!releaseRepository.existsById(id)) {
            throw new RuntimeException("Release não encontrado com o id: " + id);
        }
        releaseRepository.deleteById(id);
    }

    @Transactional
    public Page<ReleaseEntity> findAll(Pageable pageable) {
        return releaseRepository.findAll(pageable);
    }

    @Transactional
    public void addCommitsToRelease(Long id, AddCommitsRequestDTO addCommitsRequestDTO){
        ReleaseEntity releaseEntity = findById(id).get();

        String userUpdate = SecurityContextHolder.getContext().getAuthentication().getName();

        releaseEntity.getCommits().addAll(addCommitsRequestDTO.commits());
        releaseEntity.setUserUpdate(userUpdate);

        releaseRepository.save(releaseEntity);
    }
}
