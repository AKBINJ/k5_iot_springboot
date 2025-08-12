package com.example.k5_iot_springboot.controller;

import com.example.k5_iot_springboot.entity.A_Test;
import com.example.k5_iot_springboot.service.A_TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @Controller
// : 웹 요청을 처리하는 클래스 임을 명시 (반환되는 데이터 타입이 유연 - JSP, Thymeleaf 등)
// @ResponseBody
// - 데이터를 반환할 때 HTTP 응답 본문에 직접 출력 / 뷰 리졸버를 거치지 않음 / 데이터 직렬화 수행 (JSON, XML 등)
@RestController
// : @Controller + @ResponseBody
@RequestMapping("/test")
// : 클라이언트의 특정 URI로 요청이 올 때, 특정 클래스나 메서드와 연결시켜주는 어노테이션 (매핑을 담당)
// >> @RequestMapping("URI경로")
public class A_TestController {
    // controller: 클라이언트의 요청을 "처리"
    //      >> service(비즈니스 로직)에 전달

    @Autowired // 필드 주입 방식
    A_TestService testService;

    // @메서드Mapping("추가URI지정")
    // : 메서드(POST/GET/PUT/DELETE) + localhost:8080/RequestMapping경로/추가URI
    @PostMapping
    public A_Test createTest(@RequestBody A_Test test) {
        A_Test result = testService.createTest(test);
        return result;
    }

    // 요청 구조: HTTP 메서드 + URI 경로 (URI 자원에 어떠한 HTTP 동작을 실행할 것인지 명시하여 요청)
    @GetMapping("/all")
    public List<A_Test> getAllTest() {
        List<A_Test> result = testService.getAllTests();
        return result;
    }

    @GetMapping("/{testId}")
    public A_Test getTestByTestId(@PathVariable Long testId) {
        A_Test result = testService.getTestByTestId(testId);
        return result;
    }

    @PutMapping("/{testId}")
    public A_Test updateTest(@PathVariable Long testId, @RequestBody A_Test test) {
        A_Test result = testService.updateTest(testId, test);
        return result;
    }

    @DeleteMapping("/{testId}")
    public void deleteTest(@PathVariable Long testId) {
        testService.deleteTest(testId);
    }


}
