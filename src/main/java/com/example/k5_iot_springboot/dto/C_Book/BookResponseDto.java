package com.example.k5_iot_springboot.dto.C_Book;

import com.example.k5_iot_springboot.entity.C_Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDto {
    private String writer;
    private String title;
    private C_Category category;
}
