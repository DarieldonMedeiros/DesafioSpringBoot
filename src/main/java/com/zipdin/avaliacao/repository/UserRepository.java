package com.zipdin.avaliacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.zipdin.avaliacao.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{

    UserDetails findByLogin(String login);
}
