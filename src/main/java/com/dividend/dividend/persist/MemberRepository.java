package com.dividend.dividend.persist;

import com.dividend.dividend.persist.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    boolean existsByUsername(String username);
}
