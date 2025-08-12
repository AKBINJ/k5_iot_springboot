package com.example.k5_iot_springboot.service;

import com.example.k5_iot_springboot.entity.A_Test;
import org.springframework.stereotype.Service;

import java.util.List;


public interface A_TestService {
    A_Test createTest(A_Test test);

    List<A_Test> getAllTests();

    A_Test getTestByTestId(Long testId);

    A_Test updateTest(Long testId, A_Test test);

    void deleteTest(Long testId);
}
