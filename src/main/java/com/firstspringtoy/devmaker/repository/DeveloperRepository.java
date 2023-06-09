package com.firstspringtoy.devmaker.repository;


import com.firstspringtoy.devmaker.code.StatusCode;
import com.firstspringtoy.devmaker.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {

    Optional<Developer> findByMemberId(String memberId);

    List<Developer> findDeveloperByStatusCodeEquals(StatusCode statusCode);
}
