package com.dividend.dividend.persist;

import com.dividend.dividend.persist.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    boolean existsByUsername(String username);

    Optional<MemberEntity> findByUsername(String username);
}
