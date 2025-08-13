package com.example.k5_iot_springboot.common.contants;

// URI 설계 패턴
// : RESTful하게 API 경로를 규칙적으로 설계하는 것
// - 각 Controller의 고유 경로를 지정
public class ApiMappingPattern {
    // == 공통 베이스/버전 == //
    public static final String API = "/api";
    public static final String V1 = "/v1";
    public static final String BASE = API + V1;

    // == 1. 책(C_Book) == //
    public static final class Books {
        private Books() {}

        public static final String ROOT = BASE + "/books";
    }

    public static final String BOOK_API = "/api/v1/books";
}
