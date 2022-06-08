package com.capstone15.alterra.repository;

import com.capstone15.alterra.domain.dao.CommentDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentDao, Long> {
}
