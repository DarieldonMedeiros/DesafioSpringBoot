package com.zipdin.avaliacao.controller;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zipdin.avaliacao.dto.ReleaseDTO;
import com.zipdin.avaliacao.entities.ReleaseEntity;
import com.zipdin.avaliacao.repository.ReleaseRepository;
import com.zipdin.avaliacao.services.ReleaseServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/releases")
public class ReleaseController {

    private final ReleaseServices releaseServices;

    final ReleaseRepository releaseRepository;

    public ReleaseController(ReleaseRepository releaseRepository, ReleaseServices releaseServices) {
        this.releaseRepository = releaseRepository;
        this.releaseServices = releaseServices;
    }

    @PostMapping
    public ResponseEntity<Object> createRelease(@RequestBody @Valid ReleaseDTO releaseDTO){
        var releaseEntity = new ReleaseEntity();
        BeanUtils.copyProperties(releaseDTO, releaseEntity);
        releaseEntity.setReleasedAt(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(releaseServices.save(releaseEntity));
    }

}
