package com.meowzip.coparent.repository;

import com.meowzip.coparent.entity.CoParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoParentRepository extends JpaRepository<CoParent, Long> {
}
