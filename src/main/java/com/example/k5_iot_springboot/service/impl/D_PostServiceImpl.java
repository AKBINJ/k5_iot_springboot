package com.example.k5_iot_springboot.service.impl;

import com.example.k5_iot_springboot.dto.D_Post.request.PostCreateRequestDto;
import com.example.k5_iot_springboot.dto.D_Post.request.PostUpdateRequestDto;
import com.example.k5_iot_springboot.dto.D_Post.response.PostDetailResponseDto;
import com.example.k5_iot_springboot.dto.D_Post.response.PostListResponseDto;
import com.example.k5_iot_springboot.dto.ResponseDto;
import com.example.k5_iot_springboot.entity.D_Post;
import com.example.k5_iot_springboot.repository.D_PostRepository;
import com.example.k5_iot_springboot.service.D_PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본 읽기 전용 (클래스 기본), 변경 메서드만 @Transactional
public class D_PostServiceImpl implements D_PostService {

    private final D_PostRepository postRepository;

    // 1) 게시글 생성
    @Override
    @Transactional // 쓰기 트랜잭션
    public ResponseDto<PostDetailResponseDto> createPost(PostCreateRequestDto dto) {
        // dto 자체가 null인지 즉시 방어 (null인 경우 NPE 발생)
        Objects.requireNonNull(dto, "PostCreateRequestDto must not be null");

        String title = dto.title().trim();
        String content = dto.content().trim();
        String author = dto.author().trim();

        D_Post post = D_Post.create(title, content, author);
        D_Post saved = postRepository.save(post);

        return ResponseDto.setSuccess("SUCCESS", PostDetailResponseDto.from(saved));
    }

    @Override
    public ResponseDto<PostDetailResponseDto> getPostById(Long id) {
        Long pid = requirePositiveId(id);

        D_Post post = postRepository.findByIdWithComments(pid)
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 게시글을 찾을 수 없습니다."));

        return ResponseDto.setSuccess("SUCCESS", PostDetailResponseDto.from(post));
    }

    @Override
    public ResponseDto<List<PostListResponseDto>> getAllPosts() {
        List<D_Post> posts = postRepository.findAllOrderByIdDesc(); // 최신순 반환
        List<PostListResponseDto> result = posts.stream()
                .map(PostListResponseDto::from)
                .map(dto -> dto.summarize(10))
                .toList();

        return ResponseDto.setSuccess("SUCCESS", result);
    }

    @Override
    @Transactional
    public ResponseDto<PostDetailResponseDto> updatePost(Long id, PostUpdateRequestDto dto) {
        Objects.requireNonNull(dto, "PostUpdateRequestDto must not be null");
        Long pid = requirePositiveId(id);

        D_Post post = postRepository.findByIdWithComments(pid)
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 게시글을 찾을 수 없습니다."));

        post.changeTitle(dto.title().trim());
        post.changeContent(dto.content().trim());

        // Dirty Checking으로 저장 (영속성 컨텍스트에 담긴 엔티티의 상태 변화를 자동 감지)

        return ResponseDto.setSuccess("SUCCESS", PostDetailResponseDto.from(post));
    }

    @Override
    @Transactional
    public ResponseDto<Void> deletePost(Long id) {
        D_Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 게시글을 찾을 수 없습니다."));

        // orphanRemoval & cascade 설정으로 댓글은 자동 정리
        postRepository.delete(post);
        return ResponseDto.setSuccess("SUCCESS", null);
    }

    @Override
    public ResponseDto<List<PostListResponseDto>> getPostsByAuthor(String author) {
        return null;
    }

    // === 내부 유틸 메서드 === //
    private Long requirePositiveId(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("id는 반드시 양수여야 합니다.");
        return id;
    }

    private String requireNonBlank(String s, String fieldName) {
        if (!StringUtils.hasText(s)) throw new IllegalArgumentException(fieldName + " 비워질 수 없습니다.");
        // StringUtils.hasText(s)
        // : Spring Framework에서 제공하는 메서드
        // - 문자열이 "의미 있는 글자"를 가지고 있는지를 확인

        // hasText(s)
        // - null 이면 false
        // - s의 길이가 0이면 false
        // - s가 공백 문자만 있으면 false
        // >> 그 외에 실제 텍스트가 있으면 true 반환
        return s;
    }
}