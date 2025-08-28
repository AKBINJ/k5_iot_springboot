package com.example.k5_iot_springboot.service.impl;

import com.example.k5_iot_springboot.dto.H_Article.request.ArticleCreateRequest;
import com.example.k5_iot_springboot.dto.H_Article.request.ArticleUpdateRequest;
import com.example.k5_iot_springboot.dto.H_Article.response.ArticleDetailResponse;
import com.example.k5_iot_springboot.dto.H_Article.response.ArticleListResponse;
import com.example.k5_iot_springboot.dto.ResponseDto;
import com.example.k5_iot_springboot.entity.G_User;
import com.example.k5_iot_springboot.entity.H_Article;
import com.example.k5_iot_springboot.repository.G_UserRepository;
import com.example.k5_iot_springboot.repository.H_ArticleRepository;
import com.example.k5_iot_springboot.security.UserPrincipal;
import com.example.k5_iot_springboot.service.H_ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class H_ArticleServiceImpl implements H_ArticleService {
    private final H_ArticleRepository articleRepository;
    private final G_UserRepository userRepository;

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public ResponseDto<ArticleDetailResponse> createArticle(UserPrincipal principal, ArticleCreateRequest request) {
        // 유효성 검사
        validateTitleAndContent(request.title(), request.content());

        // 작성자 조회
        final String loginId = principal.getUsername();
        G_User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("AUTHOR_NOT_FOUND"));

        // 엔티티 생성 및 저장
        //H_Article article = H_Article.create(request.title(), request.content(), author);

        H_Article saved = articleRepository.save(H_Article.create(request.title(), request.content(), user));
        ArticleDetailResponse data = ArticleDetailResponse.from(saved);
        return ResponseDto.setSuccess("SUCCESS", data);
    }

    @Override
    public ResponseDto<List<ArticleListResponse>> getAllArticles() {
        List<ArticleListResponse> data = null;

        data = articleRepository.findAll().stream()
                .map(ArticleListResponse::from)
                .toList();

        return ResponseDto.setSuccess("SUCCESS", data);
    }

    @Override
    public ResponseDto<ArticleDetailResponse> getArticleById(Long id) {
        ArticleDetailResponse data = null;

        if (id == null) throw new IllegalArgumentException("ARTICLE_ID_REQUIRED");

        H_Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ARTICLE_NOT_FOUND"));
        data = ArticleDetailResponse.from(article);

        return ResponseDto.setSuccess("SUCCESS", data);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or @authz.isArticleAuthor(#id, authentication)")
    // Bean으로 등록된 AuthorizationChecker를 어노테이션화 한 기능
    // cf) PreAuthorize | PostAuthorize 내부의 기본 변수
    //      - authentication : 현재 인증 객체 (자동 캐치)
    //      - principal: authentication.getPrincipal() (주로 UserDetails 구현체)
    //      - #변수명: 메서드 파라미터 중 이름이 해당 변수명인 데이터
    public ResponseDto<ArticleDetailResponse> updateArticle(UserPrincipal principal, Long articleId, ArticleUpdateRequest request) {
        validateTitleAndContent(request.title(), request.content());

        if (articleId == null) throw new IllegalArgumentException("ARTICLE_ID_REQUIRED");

        H_Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("ARTICLE_NOT_FOUND"));

        article.update(request.title(), request.content());

        articleRepository.flush();

        ArticleDetailResponse data = ArticleDetailResponse.from(article);

        return ResponseDto.setSuccess("SUCCESS", data);

    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @authz.isArticleAuthor(#id, authentication)")
    public ResponseDto<Void> deleteArticle(UserPrincipal principal, Long id) {
        if (id == null) throw new IllegalArgumentException("ARTICLE_ID_REQUIRED");

        H_Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ARTICLE_NOT_FOUND"));

        articleRepository.delete(article);

        return ResponseDto.setSuccess("SUCCESS", null);


    }

    /** 공통 유틸: 제목/내용 유효성 검사 */
    private void validateTitleAndContent(String title, String content) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("TITLE_REQUIRED");
        }
        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("CONTENT_REQUIRED");
        }
    }
}
