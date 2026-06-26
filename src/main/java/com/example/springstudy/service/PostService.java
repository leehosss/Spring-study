package com.example.springstudy.service;

import com.example.springstudy.entity.Post;
import com.example.springstudy.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 게시글 목록 (페이징)
    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    // 게시글 단건 조회 + 조회수 증가 (한 트랜잭션 안에서 처리)
    @Transactional
    public Post findByIdAndIncrementView(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + id));
        post.setViewCount(post.getViewCount() + 1); // dirty checking으로 자동 UPDATE
        return post;
    }

    // 게시글 단건 조회 (조회수 증가 없이)
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + id));
    }

    @Transactional
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}
