package com.capstone15.alterra.repository;

import com.capstone15.alterra.domain.dao.ThreadDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThreadRepository extends JpaRepository<ThreadDao, Long> {

    @Query(value = "SELECT b FROM ThreadDao b WHERE upper(b.title) LIKE UPPER(CONCAT('%', :title, '%') ) ")
    List<ThreadDao> findAllThreadByTitle(@Param("title") String title);

    List<ThreadDao>findThreadDaoByCategoryCategoryName(String categoryName);

}
