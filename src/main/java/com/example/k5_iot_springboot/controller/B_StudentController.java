package com.example.k5_iot_springboot.controller;

import com.example.k5_iot_springboot.dto.B_Student.StudentCreateRequestDto;
import com.example.k5_iot_springboot.dto.B_Student.StudentResponseDto;
import com.example.k5_iot_springboot.dto.B_Student.StudentUpdateRequestDto;
import com.example.k5_iot_springboot.service.B_StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

// cf) RESTful API: REST API를 잘 따르는 아키텍처 스타일

// cf) RequestMapping 베스트 프렉티스
//      : 주로 버저닝(/api/v1) + 복수형태의 명사(/students) 같이 사용

@RestController // @Controller + @ResponseBody (RESTful 웹 서비스의 컨트롤러 임을 명시)
@RequestMapping("/api/v1/students") // 컨트롤러의 공동 URL prefix (아래 메서드 경로는 모두 /students로 시작)
@RequiredArgsConstructor
public class B_StudentController {
    // 비즈니스 로직을 처리하는 service 객체 주입 (생성자 주입)
    private final B_StudentService studentService;

    // 응답, 요청에 대한 데이터 정의 (Request & Response)

    // 1) 새로운 학생 등록(POST)
    // - 성공 201 Created + Location 헤더(/students/{id}) + 생성 데이터
    // cf) 리소스 생성 성공은 201 Created가 표준!
    @PostMapping
    public ResponseEntity<StudentResponseDto> createStudent(@RequestBody StudentCreateRequestDto requestDto
            , UriComponentsBuilder uriComponentsBuilder) {
        StudentResponseDto created = studentService.createStudent(requestDto);

        // Location 헤더 생성
        // : 서버의 응답이 다른 곳에 있음을 알려주고 해당 위치(URI)를 지정
        // - 리다이렉트 할 페이지의 URL을 나타냄
        // - 201 (Created), 3XX (redirection) 응답 상태와 주로 사용
        URI location = uriComponentsBuilder // 현재 HTTP 요청의 정보를 바탕으로 설정
                .path("/{id}") // 현재 경로 + /{id}
                .buildAndExpand(created.getId()) // 템플릿 변수 치환 - 동적 데이터 처리
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    // 2) 전체 학생 목록 조회 (GET)
    @GetMapping
    public ResponseEntity<List<StudentResponseDto>> getAllStudents() {
        List<StudentResponseDto> result = studentService.getAllStudents();
        return ResponseEntity.ok(result);
    }

    // 3) 특정 학생 조회 (GET + /{id})
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> getStudentById(@PathVariable Long id) {
        StudentResponseDto result = studentService.getStudentById(id);
        return ResponseEntity.ok(result);
    }

    // 4) 특정 학생 수정 (PUT + /{id})
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentUpdateRequestDto requestDto
    ) {
        StudentResponseDto updated = studentService.updateStudent(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    // 5) 특정 학생 삭제 (DELETE + /{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    // 6) 학생 필터링 조회 (이름 검색)
    // GET + "/filter?name=값"
    @GetMapping("/filter")
    public ResponseEntity<List<StudentResponseDto>> filterStudentsByName(@RequestParam String name) {
        List<StudentResponseDto> result = studentService.filterStudentsByName(name);
        return ResponseEntity.ok(result);
    }
}