package com.example.k5_iot_springboot.service;

import com.example.k5_iot_springboot.dto.ResponseDto;
import com.example.k5_iot_springboot.dto.C_Book.BookCreateRequestDto;
import com.example.k5_iot_springboot.dto.C_Book.BookResponseDto;
import com.example.k5_iot_springboot.dto.C_Book.BookUpdateRequestDto;
import com.example.k5_iot_springboot.entity.C_Category;

import java.util.List;

public interface C_BookService {
    ResponseDto<BookResponseDto> createBook(BookCreateRequestDto dto);

    ResponseDto<List<BookResponseDto>> getAllBooks();

    ResponseDto<BookResponseDto> getBookById(Long id);

    ResponseDto<BookResponseDto> updateBook(Long id, BookUpdateRequestDto dto);

    ResponseDto<Void> deleteBook(Long id);

    ResponseDto<List<BookResponseDto>> getBooksByTitleContaining(String keyword);

    ResponseDto<List<BookResponseDto>> getBooksByCategory(C_Category category);

}
