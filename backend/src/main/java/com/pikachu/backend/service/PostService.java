package com.pikachu.backend.service;

import com.pikachu.backend.dto.PostRequest;
import com.pikachu.backend.dto.PostResponse;
import com.pikachu.backend.entity.Post;
import com.pikachu.backend.entity.User;
import com.pikachu.backend.exception.ResourceNotFoundException;
import com.pikachu.backend.exception.UnauthorizedException;
import com.pikachu.backend.repository.PostRepository;
import com.pikachu.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    // 게시물 생성

    public PostResponse createPost(PostRequest request, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

        Post post = new Post();
        post.setContent(request.getContent());
        post.setAuthor(author);

        Post savedPost = postRepository.save(post);
        return PostResponse.from(savedPost);
    }


    // 게시물 목록 조회 (페이징)
    @Transactional(readOnly = true)
    public Page<PostResponse> getPosts(Pageable pageable) {
        return postRepository.findAllWithAuthor(pageable)
                .map(PostResponse::from);
    }


    // 특정 게시물 조회
    @Transactional(readOnly = true)
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findByIdWithAuthorAndLikes(postId)
                .orElseThrow(() -> new ResourceNotFoundException("게시물을 찾을 수 없습니다"));

        return PostResponse.from(post);
    }

    // 게시물 수정
    public PostResponse updatePost(Long postId, PostRequest request, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("게시물을 찾을 수 없습니다"));

        // 권한 확인
        if (!post.getAuthor().getUsername().equals(username)) {
            throw new UnauthorizedException("게시물을 수정할 권한이 없습니다");
        }

        post.setContent(request.getContent());
        Post updatedPost = postRepository.save(post);

        return PostResponse.from(updatedPost);
    }

    // 게시물 삭제
    public void deletePost(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("게시물을 찾을 수 없습니다"));

        // 권한 확인
        if (!post.getAuthor().getUsername().equals(username)) {
            throw new UnauthorizedException("게시물을 삭제할 권한이 없습니다");
        }

        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUserId(Long userId) {
        return postRepository.findByAuthorId(userId, PageRequest.of(0,20))
                .getContent()
                .stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());
    }

    public Page<PostResponse> getUserPosts(Long userId, Pageable pageable) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        Page<Post> posts = postRepository.findByAuthorId(userId, pageable);
        return posts.map(post -> {
            PostResponse response = PostResponse.from(post);
//            Long likeCount = likeRepository.countByPostId(post.getId());
//            boolean isLiked = likeRepository.existsByUserAndPost(currentUser, post);
//            Long commentCount = commentRepository.countByPostId(post.getId());

//            response.setLikeCount(likeCount);
//            response.setLiked(isLiked);
//            response.setCommentCount(commentCount);

            return response;
        });
    }

    public Long getUserPostCount(Long userId) {
        return postRepository.countByAuthorId(userId);
    }
}
