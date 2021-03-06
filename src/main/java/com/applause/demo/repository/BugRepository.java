package com.applause.demo.repository;

import com.applause.demo.entity.Bug;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BugRepository extends CrudRepository<Bug, Long> {
}
