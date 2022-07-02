package com.capstone15.alterra.repository;

import com.capstone15.alterra.domain.dao.ThreadDao;
import com.capstone15.alterra.domain.dao.UserDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ThreadRepository extends JpaRepository<ThreadDao, Long> {

    @Query(value = "SELECT b FROM ThreadDao b WHERE upper(b.title) LIKE UPPER(CONCAT('%', :title, '%') ) ")
    List<ThreadDao> findAllThreadByTitle(@Param("title") String title);

    List<ThreadDao>findThreadDaoByCategoryCategoryName(String categoryName);

    @Query(value = "SELECT t FROM ThreadDao t ORDER BY t.thread_likes DESC")
    List<ThreadDao> findAllPopularThread();

    Page<ThreadDao> findAllBy(Pageable pageable);

    @Query(value = "SELECT count(tf) FROM ThreadDao tf WHERE tf.isDeleted = false AND tf.user.id = :id ")
    Integer countThreads(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM m_thread WHERE user_id = :id", nativeQuery = true)
    void deleteCustom(@Param("id") Long id);

//    @Modifying
//    @Query(value = "DELETE FROM ThreadDao t WHERE t.user.id = :id")
//    void deleteCustom(@Param("id") Long id);

}
