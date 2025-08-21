package com.example.k5_iot_springboot.controller;

import com.example.k5_iot_springboot.common.constants.ApiMappingPattern;
import com.example.k5_iot_springboot.dto.F_Board.request.BoardRequestDto;
import com.example.k5_iot_springboot.dto.F_Board.response.BoardResponseDto;
import com.example.k5_iot_springboot.dto.ResponseDto;
import com.example.k5_iot_springboot.service.F_BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    // 2) 게시글 조회
    @GetMapping(ApiMappingPattern.Boards.ID_ONLY)
    public ResponseEntity<ResponseDto<List<BoardResponseDto.SummaryResponse>>> getAllBoards() {
        ResponseDto<List<BoardResponseDto.SummaryResponse>> response = boardService.getAllBoards();
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
