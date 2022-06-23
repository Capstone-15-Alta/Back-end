package com.capstone15.alterra.repository;

import com.capstone15.alterra.domain.dao.ThreadReportDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadReportRepository extends JpaRepository<ThreadReportDao, Long> {
}
