package com.example.k5_iot_springboot.dto.F_Board.response;

import com.example.k5_iot_springboot.common.utils.DateUtils;
import com.example.k5_iot_springboot.entity.F_Board;

/**
 * 게시글 응답 DTO 모음
 * - 프론트 사용성을 높이기 위해 KST 문자열과 UTC ISO 문자열을 함께 제공
 * */
public class BoardResponseDto {

    /**
     * 게시글 상세 응답
     * */
    public record DetailResponse(
            Long id,
            String title,
            String content,
            String createdAtKst,
            String updatedAtKst,
            String createdUtcIso,
            String updatedUtcIso
    ) {
        public static DetailResponse from(F_Board board) {
            return new DetailResponse(
                    board.getId(),
                    board.getTitle(),
                    board.getContent(),
                    DateUtils.toKstString(board.getCreatedAt()),
                    DateUtils.toKstString(board.getUpdatedAt()),
                    DateUtils.toUtcString(board.getCreatedAt()),
                    DateUtils.toUtcString(board.getUpdatedAt())
            );
        }
    }

    /**
     * 게시글 목록/요약 응답
     * - 목록 API에서 가벼운 페이로드가 필요할 때 사용
     *
     * cf) 페이로드(payload)
     *      : 전송되는 데이터 중 실질적인 정보
     * */
    public record SummaryResponse(
            Long id,
            String title,
            String createdAtKst,
            String createdUtcIso
    ) {
        public static SummaryResponse from(F_Board board) {
            return new SummaryResponse(
                    board.getId(),
                    board.getTitle(),
                    DateUtils.toKstString(board.getCreatedAt()),
                    DateUtils.toUtcString(board.getCreatedAt())
            );
        }
    }
}