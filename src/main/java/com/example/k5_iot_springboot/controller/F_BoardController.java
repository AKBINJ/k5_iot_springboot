package com.example.k5_iot_springboot.controller;

import com.example.k5_iot_springboot.common.constants.ApiMappingPattern;
import com.example.k5_iot_springboot.dto.F_Board.request.BoardRequestDto;
import com.example.k5_iot_springboot.dto.F_Board.response.BoardResponseDto;
import com.example.k5_iot_springboot.dto.ResponseDto;
import com.example.k5_iot_springboot.service.F_BoardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.Boards.ROOT)
@RequiredArgsConstructor
@Validated
public class F_BoardController {
    private final F_BoardService boardService;

    // 1) 게시글 생성
    @PostMapping
    public ResponseEntity<ResponseDto<BoardResponseDto.DetailResponse>> createBoard(
            @Valid @RequestBody BoardRequestDto.CreateRequest request
    ) {
        ResponseDto<BoardResponseDto.DetailResponse> response = boardService.createBoard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2) 게시글 조회 (전체 조회)
    @GetMapping("/all")
    public ResponseEntity<ResponseDto<List<BoardResponseDto.SummaryResponse>>> getAllBoards() {
        ResponseDto<List<BoardResponseDto.SummaryResponse>> response = boardService.getAllBoards();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 2-1) 게시글 조회 (페이지네이션 OffSet 조회)
    @GetMapping
    public ResponseEntity<ResponseDto<BoardResponseDto.PageResponse>> getBoardsPage(
//            // page: 0부터 시작, 필요 시 1부터 시작하는 정책도 가능
//            @RequestParam(defaultValue = "0") @Min(0) int page,
//            // size: 최대 100 제한 (과도한 요청 방지)
//            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
//            // sort: 여러 개 허용 - EX) sort=createAt,desc&sort=title,asc
//            @RequestParam(required = false) String sort
            @PageableDefault(
                    page = 0,           // 기본 페이지 번호
                    size = 10,          // 기본 페이지 크기
                    sort = "createdAt", // 기본 정렬 컬럼
                    direction = Sort.Direction.DESC // 기본 정렬 방향
            ) Pageable pageable
    ) {
//        ResponseDto<BoardResponseDto.PageResponse> response = boardService.getBoardsPage(page, size, sort);
        ResponseDto<BoardResponseDto.PageResponse> response = boardService.getBoardsPage(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 2-2) 게시글 조회 (페이지네이션 Cursor 조회)
    @GetMapping("/cursor")
    public ResponseEntity<ResponseDto<BoardResponseDto.SliceResponse>> getBoardsByCursor(
            // 처음 요청이면 null >> 가장 최신부터 시작
            // : 목록을 항상 하나의 정렬 기준으로 고정! (id DESC - 최신 글 먼저)
            // > 다음 페이지를 가져올 때는 기준 커서보다 더 오래된 (작은 id) 행만 가져오기
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        ResponseDto<BoardResponseDto.SliceResponse> response = boardService.getBoardsByCursor(cursorId, size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 3) 게시글 수정
    @PutMapping(ApiMappingPattern.Boards.ID_ONLY)
    public ResponseEntity<ResponseDto<BoardResponseDto.DetailResponse>> updateBoard(
            @PathVariable Long boardId,
            @Valid @RequestBody BoardRequestDto.UpdateRequest request
    ) {
        ResponseDto<BoardResponseDto.DetailResponse> response = boardService.updateBoard(boardId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}