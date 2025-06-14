package com.zipdin.avaliacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zipdin.avaliacao.entities.ReleaseEntity;

@Repository
public interface ReleaseRepository extends JpaRepository<ReleaseEntity, Long>{

}
