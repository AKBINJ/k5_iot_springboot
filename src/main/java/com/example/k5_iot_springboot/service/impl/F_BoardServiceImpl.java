package com.example.k5_iot_springboot.service.impl;

import com.example.k5_iot_springboot.dto.F_Board.request.BoardRequestDto;
import com.example.k5_iot_springboot.dto.F_Board.response.BoardResponseDto;
import com.example.k5_iot_springboot.dto.ResponseDto;
import com.example.k5_iot_springboot.entity.F_Board;
import com.example.k5_iot_springboot.repository.F_BoardRepository;
import com.example.k5_iot_springboot.service.F_BoardService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class F_BoardServiceImpl implements F_BoardService {
    private final F_BoardRepository boardRepository;

    // == 페이지네이션 공통: 안전한 Pageable 생성(화이트리스트 정렬) ==
    // : 정렬 키를 그대로 신뢰할 경우, 존재하지 않는 필드 또는 JPA 동적 JPQL에서 문자열 충돌 발생 가능
    private static final Set<String> ALLOWED_SORTS = Set.of("id", "title", "createdAt", "updatedAt");

    private Pageable buildPageable(int page, int size, String[] sortParams) {
        // 정렬 파라미터 파싱: ["createdAt,desc", "title,asc"] 형태
        Sort sort = Sort.by("createdAt").descending(); // 기본 정렬: 최신순
        // >> 정렬 파라미터가 없거나, 전부 화이트리스트에서 무시된 경우 디폴트 정렬을 사용

        if (sortParams != null && sortParams.length > 0) { // 빈 배열이 아닌 경우 (요소 1개 이상)
            // 정렬 순서를 보장할 리스트 - 여러 정렬 기준을 저장 (순서 보장!)
            List<Sort.Order> orders = new ArrayList<>();
            for (String p: sortParams) {
                if (p == null || p.isBlank()) continue;
                String[] t = p.split(",");
                String property = t[0].trim();

                // 화이트리스트에 없는 속성은 무시
                if (!ALLOWED_SORTS.contains(property)) continue;

                Sort.Direction dir = Sort.Direction.DESC;
                // 기본 정렬 방향을 DESC - 피드/게시물은 최신순 정렬이 일반 (권장)
                if (t.length > 1) { // 정렬기준이 존재
                    dir = "asc".equalsIgnoreCase(t[1].trim())
                            ? Sort.Direction.ASC
                            : Sort.Direction.DESC;
                }
                orders.add(new Sort.Order(dir, property));
                // : 파싱한 정렬 기준 한 건을 Sort.Order 객체로 만들어 목록에 추가
                // - 여러 건이 쌓이면 ORDER BY prop1 dir1, prop2 dir2 ... 순서대로 적용
            }
            if (!orders.isEmpty()) sort = Sort.by(orders); // 비워지지 않은 경우 sort값 재할당
        }
        return PageRequest.of(page, size, sort);
        // sortParams가 비워진 경우 || 유효한 정렬이 없는 경우
    }

    @Transactional
    @Override
    public ResponseDto<BoardResponseDto.DetailResponse> createBoard(BoardRequestDto.@Valid CreateRequest request) {
        F_Board board = F_Board.builder()
                .title(request.title())
                .content(request.content())
                .build();

        F_Board saved = boardRepository.save(board);

        BoardResponseDto.DetailResponse result
                = BoardResponseDto.DetailResponse.from(saved);

        return ResponseDto.setSuccess("SUCCESS", result);
    }

    @Override
    public ResponseDto<List<BoardResponseDto.SummaryResponse>> getAllBoards() {
        List<F_Board> boards = boardRepository.findAll();

        List<BoardResponseDto.SummaryResponse> result = boards.stream()
                .map(BoardResponseDto.SummaryResponse::from)
                .toList();

        return ResponseDto.setSuccess("SUCCESS", result);
    }

    @Transactional
    @Override
    public ResponseDto<BoardResponseDto.DetailResponse> updateBoard(Long boardId, BoardRequestDto.@Valid UpdateRequest request) {
        F_Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 게시글을 찾을 수 없습니다."));

        board.update(request.title(), request.content());

        // F_Board saved = boardRepository.save(board);
        //BoardResponseDto.DetailResponse result = BoardResponseDto.DetailResponse.from(saved);

        // cf) updatedAt의 데이터 확인
        //      : JPA Auditing이 flush/commit 시점에 @PreUpdate가 실행되면 채워짐
        //      - 영속성 컨텍스트가 DB에 반영될 때
        //      >> 서비스 안에서 DTO 변환이 곧바로 일어날 때 updatedAt이 갱신 전 값으로 보여짐
        //          +) 다시 실행 시 커밋된 변경사항 확인 가능

        // cf) save() VS flush()
        // 1) save()
        // : Spring Data JPA Repository 메서드
        // - 새로운 엔티티 INSERT, 이미 존재하는 엔티티 UPDATE
        //      >> 영속 상태를 처리
        //      +) findById로 가져온 엔티티는 이미 영속 상태를 가져 save() 안해도 커밋 시점에 자동 UPDATE

        // 2) flush()
        // : JPA(EntityManager) 메서드
        // - 해당 시점까지 영속성 컨텍스트(1차 캐시)에 쌓인 변경 내역(Dirty Checking 결과)을
        //      , 즉시 DB에 반영
        //      >> 트랜잭션은 열린 상태 (커밋 X)

        boardRepository.flush(); // 변경 내용을 DB에 반영 (@PreUpdate 트리거 >> updatedAt 채워짐)
        BoardResponseDto.DetailResponse result = BoardResponseDto.DetailResponse.from(board);

        return ResponseDto.setSuccess("SUCCESS", result);
    }

    // cf) Page<T> VS Slice<T>
    // 1) Page<T>
    //      : 전체 개수(count 쿼리)까지 실행해서 가져옴

    // 2) Slice<T>
    //      : count 쿼리 실행 X, 데이터 개수를 size + 1로 요청해서 다음 페이지 존재 여부만 판단

    @Override
    public ResponseDto<BoardResponseDto.PageResponse> getBoardsPage(Pageable pageable) {
//        Pageable pageable = buildPageable(page, size, sort);

        // cf) Pageable 인터페이스
        //      : 페이징과 정렬 정보를 추상화한 인터페이스
        //      >> 현재 페이지 번호, 한 페이지의 크기, 정렬 정보 반환
        //              , 다음 페이지 객체 생성, 이전 페이지 객체 생성
        //      >> 특징
        //          : 실제 구현체는 PageRequest 사용 (PageRequest.of(page, size, sort))
        //          : JpaRepository의 findAll(Pageable pageable) 메서드에 전달


        Page<F_Board> pageResult = boardRepository.findAll(pageable);

        List<BoardResponseDto.SummaryResponse> content = pageResult.getContent().stream()
                .map(BoardResponseDto.SummaryResponse::from)
                .toList();

        BoardResponseDto.PageMeta meta = BoardResponseDto.PageMeta.from(pageResult);

        BoardResponseDto.PageResponse result = BoardResponseDto.PageResponse.builder()
                .content(content)
                .meta(meta)
                .build();

        return ResponseDto.setSuccess("SUCCESS", result);
    }

    @Override
    public ResponseDto<BoardResponseDto.SliceResponse> getBoardsByCursor(Long cursorId, int size) {
        // 커서는 최신순 id 기준으로 진행 (성능이 좋은 PK 정렬)
        // - 첫 호출: cursorId == null (Long.MAX_VALUE로 간주: 최신부터)
        long startId = (cursorId == null) ? Long.MAX_VALUE : cursorId;

        Slice<F_Board> slice = boardRepository
                .findByIdLessThanOrderByIdDesc(startId, PageRequest.of(0, size));

        List<BoardResponseDto.SummaryResponse> content = slice.getContent().stream()
                .map(BoardResponseDto.SummaryResponse::from)
                .toList();

        Long nextCursor = null;
        if (!content.isEmpty()) {
            nextCursor = content.get(content.size() - 1).id(); // 마지막 아이템 id
        }

        BoardResponseDto.SliceResponse result = BoardResponseDto.SliceResponse.builder()
                .content(content)
                .hasNext(slice.hasNext())
                .nextCursor(nextCursor)
                .build();

        return ResponseDto.setSuccess("SUCCESS", result);
    }
}