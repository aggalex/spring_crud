package com.backend.dove.repository;

import com.backend.dove.entity.Blacklist;
import com.backend.dove.entity.Whitelist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WhitelistRepository extends JpaRepository<Whitelist, Long> {

    boolean existsByEmail(String email);

}
