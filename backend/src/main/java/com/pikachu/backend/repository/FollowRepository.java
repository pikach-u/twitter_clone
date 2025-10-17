package com.pikachu.backend.repository;

import com.pikachu.backend.entity.Follow;
import com.pikachu.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {


     // 팔로워와 팔로잉 관계로 Follow 엔티티 조회
    @Query("SELECT f FROM Follow f WHERE f.follower = :follower AND f.following = :following")
    Optional<Follow> findByFollowerAndFollowing(@Param("follower") User follower,
                                                @Param("following") User following);


    // 특정 사용자의 팔로잉 목록 조회
    @Query("SELECT f FROM Follow f WHERE f.following = :following ORDER BY f.createdAt DESC")
    List<Follow> findByFollowing(@Param("following") User following);


    // 특정 사용자의 팔로워 목록 조회
    @Query("SELECT f FROM Follow f WHERE f.follower = :follower ORDER BY f.createdAt DESC")
    List<Follow> findByFollower(@Param("follower") User follower);


    // 특정 사용자의 팔로잉 수 조회
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following = :following")
    long countFollowing(@Param("following") User following);


    // 특정 사용자의 팔로워 수 조회
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower = :follower")
    long countFollowers(@Param("follower") User follower);


    // 특정 사용자의 팔로우 관계 여부 확인
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Follow f WHERE f.follower = :follower AND f.following = :following")
    boolean existsByFollowerAndFollowing(@Param("follower") User follower,
                                         @Param("following") User following);
}