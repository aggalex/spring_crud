package com.backend.dove.repository;

import com.backend.dove.entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {

    boolean existsByEmail(String email);

}
