package com.example.springstudy.repository;

import com.example.springstudy.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 최신글 순으로 페이징 조회
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
