package com.firstspringtoy.devmaker.repository;


import com.firstspringtoy.devmaker.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetiredDeveloperRepository extends JpaRepository<Developer, Long> {
}
