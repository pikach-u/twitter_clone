package com.pikachu.backend.repository;

import com.pikachu.backend.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {


     // 작성자 정보를 함께 조회 (N+1 문제 방지)

    @Query("SELECT p FROM Post p JOIN FETCH p.author ORDER BY p.createdAt DESC")
    Page<Post> findAllWithAuthor(Pageable pageable);


    // 게시물 상세 조회 시 작성자와 좋아요 정보 함께 조회

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN FETCH p.author " +
            "LEFT JOIN FETCH p.likes " +
            "WHERE p.id = :postId")
    Optional<Post> findByIdWithAuthorAndLikes(@Param("postId") Long postId);


     // 특정 사용자의 게시물 조회

    @Query("SELECT p FROM Post p WHERE p.author.id = :userId ORDER BY p.createdAt DESC")
    Page<Post> findByAuthorId(@Param("userId") Long userId, Pageable pageable);
}
