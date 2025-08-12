package com.example.k5_iot_springboot.service.impl;

import com.example.k5_iot_springboot.entity.A_Test;
import com.example.k5_iot_springboot.repository.A_TestRepository;
import com.example.k5_iot_springboot.service.A_TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class A_TestServiceImpl implements A_TestService {
    private final A_TestRepository testRepository;

//    // 생성자 주입 방식
//    public A_TestServiceImpl(A_TestRepository testRepository) {
//        this.testRepository = testRepository;
//    }

    @Override
    public A_Test createTest(A_Test test) {
        return testRepository.save(test);
    }

    @Override
    public List<A_Test> getAllTests() {
        return testRepository.findAll();
    }

    @Override
    public A_Test getTestByTestId(Long testId) {
        Optional<A_Test> optionalTestEntity = testRepository.findById(testId);

        A_Test test = optionalTestEntity.orElseThrow(() ->
                new RuntimeException("해당 ID를 가진 데이터가 없습니다: " + testId));

        return test;
    }

    @Override
    public A_Test updateTest(Long testId, A_Test test) {
        A_Test originalTest = getTestByTestId(testId);

        originalTest.setName(test.getName());

        A_Test updatedTest = testRepository.save(originalTest);

        return updatedTest;
    }

    @Override
    public void deleteTest(Long testId) {
        testRepository.deleteById(testId);
    }
}
