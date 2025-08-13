package com.example.k5_iot_springboot.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum C_Category {
    // 각 Enum 상수
    // : DB에 저장될 값(dbValue)과 화면 표시용 값(label)을 함께 정의
    NOVEL("NOVEL", "소설"),
    ESSAY("ESSAY", "에세이"),
    POEM("POEM", "시"),
    MAGAZINE("MAGAZINE", "잡지"),
    ;

    private final String dbValue;
    private final String label;

    /*
        JSON 직렬화 시 동작 메서드
        : Enum >> String
        : ENUM 객체를 JSON 응답으로 변환할 때, dbValue 값을 그대로 전달
        EX) C_Category.NOVEL >>> "NOVEL"
    */
    @JsonValue
    public String toJson() {
        return dbValue;
    }

    /*
        2) JSON 역질렬화 시 동작 메서드
        : String >> Enum
        : JSON 요청 값을 Enum으로 변환할 때, Enum 이름(name)과 DB 값(dbValue) 모두 인식
        - 대소문자 구분 X, 값이 없거나 잘못된 경우 예외 발생
    */
    @JsonCreator
    public static C_Category fromJson(String value) {
        if (value == null) return null;

        String v = value.trim();

        return Arrays.stream(values())
                // Enum 데이터를 스트림으로 변경 (데이터 순회 필터링)
                .filter(c -> c.name().equalsIgnoreCase(v) || c.dbValue.equalsIgnoreCase(v))
                .findFirst() // Optional 반환
                .orElseThrow(() -> new IllegalArgumentException("Invalid category: " + value));
    }

    /*
        3) DB에서 읽어온 문자열 값을 Enum으로 변환
            : DB에 저장된 값과 정확히 일치하는 Enum을 반환 (일치하는 값 없으면 예외 발생)
    */
    public static C_Category fromDbValue(String dbValue) {
        if (dbValue == null) return null;
        return Arrays.stream(values())
                .filter(c -> c.dbValue.equals(dbValue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown category DB " + dbValue));
    }
}






























