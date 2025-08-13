package com.example.k5_iot_springboot.service.impl;

import com.example.k5_iot_springboot.dto.C_Book.BookCreateRequestDto;
import com.example.k5_iot_springboot.dto.C_Book.BookResponseDto;
import com.example.k5_iot_springboot.dto.C_Book.BookUpdateRequestDto;
import com.example.k5_iot_springboot.dto.ResponseDto;
import com.example.k5_iot_springboot.entity.C_Book;
import com.example.k5_iot_springboot.entity.C_Category;
import com.example.k5_iot_springboot.repository.C_BookRepository;
import com.example.k5_iot_springboot.service.C_BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class C_BookServiceImpl implements C_BookService {
    private final C_BookRepository bookRepository;

    @Override
    public ResponseDto<BookResponseDto> createBook(BookCreateRequestDto dto) {
        try {
            C_Book newBook = new C_Book(
                    null, dto.getWriter(), dto.getTitle(), dto.getContent(), dto.getCategory()
            );

            C_Book savedBook = bookRepository.save(newBook);

            return ResponseDto.setSuccess("책이 생성되었습니다.", toResponse(savedBook));

        } catch (Exception e) {
            return ResponseDto.setFailed("책 등록 중 문제가 발생하였습니다: " + e.getMessage());
        }
    }

    @Override
    public ResponseDto<List<BookResponseDto>> getAllBooks() {
        List<BookResponseDto> data = null;

        data = bookRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseDto.setSuccess("SUCCESS", data);
    }

    @Override
    public ResponseDto<BookResponseDto> getBookById(Long id) {
        try {
            C_Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 id의 책을 찾을 수 없습니다: " + id));

            return ResponseDto.setSuccess("SUCCESS", toResponse(book));

        } catch (Exception e) {
            return ResponseDto.setFailed("책 조회 중 문제가 발생하였습니다: " + e.getMessage());
        }
    }

    @Override
    public ResponseDto<BookResponseDto> updateBook(Long id, BookUpdateRequestDto dto) {
        try {
            C_Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 id의 책을 찾을 수 없습니다: " + id));

            applyUpdates(book, dto); // 부분 수정 - Content, Category 중 존재하는 요청 데이터만 수정 반영

            C_Book saved = bookRepository.save(book);

            return ResponseDto.setSuccess("SUCCESS", toResponse(saved));

        } catch (Exception e) {
            return ResponseDto.setFailed("책 수정 중 문제가 발생하였습니다: " + e.getMessage());
        }
    }

    @Override
    public ResponseDto<Void> deleteBook(Long id) {
        try {
            C_Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 id의 책을 찾을 수 없습니다: " + id));

            bookRepository.delete(book);
            return ResponseDto.setSuccess("SUCCESS", null);

        } catch (Exception e) {
            return ResponseDto.setFailed("책 삭제 중 문제가 발생하였습니다.");
        }
    }

    @Override
    public ResponseDto<List<BookResponseDto>> getBooksByTitleContaining(String keyword) {
        List<BookResponseDto> data = null;

        if (keyword == null || keyword.isEmpty()) {
            return ResponseDto.setFailed("검색 키워드를 입력");
        }

        List<C_Book> found = bookRepository.findByTitleContaining(keyword);

        if (found.isEmpty()) {
            return ResponseDto.setFailed("검색 결과 없음");
        }

        data = found.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseDto.setSuccess("SUCCESS", data);
    }

    @Override
    public ResponseDto<List<BookResponseDto>> getBooksByCategory(C_Category category) {
        List<BookResponseDto> data = null;
        if (category == null) {
            return ResponseDto.setFailed("카테고리를 선택");
        }
        List<C_Book> found = bookRepository.findByCategory(category);

        if (found.isEmpty()) {
            return ResponseDto.setFailed("검색 결과 없음");
        }

        data = found.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseDto.setSuccess("SUCCESS", data);
    }

    // 유틸 메서드 (Entity >> Response Dto)
    private BookResponseDto toResponse(C_Book entity) {
        BookResponseDto dto = new BookResponseDto(
                entity.getWriter(),
                entity.getTitle(),
                entity.getCategory()
        );
        return dto;
    }

    // Update 수정: 부분 수정 적용
    private void applyUpdates(C_Book entity, BookUpdateRequestDto dto) {
        if (dto.getContent() != null) entity.setContent(dto.getContent());
        if (dto.getCategory() != null) entity.setCategory(dto.getCategory());
    }
}