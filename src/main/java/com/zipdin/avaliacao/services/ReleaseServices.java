package com.zipdin.avaliacao.services;

import org.springframework.stereotype.Service;

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
}
