package com.example.k5_iot_springboot.service;

import com.example.k5_iot_springboot.dto.D_Post.request.PostCreateRequestDto;
import com.example.k5_iot_springboot.dto.D_Post.request.PostUpdateRequestDto;
import com.example.k5_iot_springboot.dto.D_Post.response.PostDetailResponseDto;
import com.example.k5_iot_springboot.dto.D_Post.response.PostListResponseDto;
import com.example.k5_iot_springboot.dto.D_Post.response.PostWithCommentCountResponseDto;
import com.example.k5_iot_springboot.dto.ResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public interface D_PostService {
    ResponseDto<PostDetailResponseDto> createPost(@Valid PostCreateRequestDto dto);

    ResponseDto<PostDetailResponseDto> getPostById(Long id);

    ResponseDto<List<PostListResponseDto>> getAllPosts();

    ResponseDto<PostDetailResponseDto> updatePost(Long id, @Valid PostUpdateRequestDto dto);

    ResponseDto<Void> deletePost(Long id);

    ResponseDto<List<PostListResponseDto>> getPostsByAuthor(String author);

    ResponseDto<List<PostListResponseDto>> searchPostByTitle(@NotBlank(message = "검색 키워드는 비어 있을 수 없습니다.") String keyword);

    ResponseDto<List<PostWithCommentCountResponseDto>> getTop5PostsByComments();

    ResponseDto<List<PostListResponseDto>> searchPostsByCommentKeyword(@NotBlank(message = "검색 키워드는 비어 있을 수 없습니다.") String keyword);

    ResponseDto<List<PostWithCommentCountResponseDto>> getAuthorPostsWithMinComments(@NotBlank(message = "작성자(author)는 비워 질 수 없습니다.") String author, int minCount);
}
