package com.example.demo.temp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRdsRepository extends JpaRepository<UserRds, Integer> {
    Optional<UserRds> findByUsername(String username);
}