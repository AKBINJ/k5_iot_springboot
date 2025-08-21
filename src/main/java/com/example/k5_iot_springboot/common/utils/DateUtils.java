package com.example.k5_iot_springboot.common.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * 날짜/시간 변환 유틸
 * - 저장은 UTC(LocalDateTime, DB DATETIME(6))
 * - 노출은 KST 문자열 또는 ISO-8601 문자열로
 * */
public class DateUtils {
    // KST(Asia/Seoul) 타임존 상수
    private static final ZoneId ZONE_KST = ZoneId.of("Asia/Seoul");

    // KST 문자열 포맷
    private static final DateTimeFormatter KST_FORMAT
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ISO-8601 형식(UTC, 'Z' 붙는 형태) 포맷
    private static final DateTimeFormatter ISO_UTC
            = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    // DB(LocalDateTime, UTC) >>> "yyyy-MM-dd HH:mm:ss"(KST) 문자열
    public static String toKstString(LocalDateTime utcLocalDateTime) {
        if (utcLocalDateTime == null) return null;
        ZonedDateTime zdtUtc = utcLocalDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime zdtKst = zdtUtc.withZoneSameInstant(ZONE_KST);
        return zdtKst.format(KST_FORMAT);
    }

    // DB >>> ISO-8601 문자열
    // : 프론트에서 타임존이 필요이 필요한 경우 유용
    public static String toUtcString(LocalDateTime utcLocalDateTime) {
        if (utcLocalDateTime == null) return null;
        // UTC로 해석한 수, Offset을 명시(+00:00)하여 문자열 생성
        OffsetDateTime odt = utcLocalDateTime.atOffset(ZoneOffset.UTC);
        return ISO_UTC.format(odt);
    }
}