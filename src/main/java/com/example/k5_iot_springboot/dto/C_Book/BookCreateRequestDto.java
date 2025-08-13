package com.example.k5_iot_springboot.dto.C_Book;

import com.example.k5_iot_springboot.entity.C_Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookCreateRequestDto {
    private String writer;
    private String title;
    private String content;
    private C_Category category;
}
