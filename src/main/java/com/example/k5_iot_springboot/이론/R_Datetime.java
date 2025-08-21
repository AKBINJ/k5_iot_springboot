package com.example.k5_iot_springboot.이론;

/*
    == 시간 + 날짜 데이터 처리 ==
    1) UTC(Coordinated Universal Time, 협정 세계시)
        - 전 세계 표준 시간, 국제적으로 사용되는 기준 시간
        - 각 나라가 사용되는 현지 시간은 모두 UTC를 기준으로 +/- 시차를 적용해 계산
        - 현지 시간을 기준으로 활용하기엔 가독성이 떨어짐
    2) KST(Korea Standard Time, 한국 표준시)
        - 대한민국에서 사용하는 공식 시간대
        - UTC와의 차이가 URC+9 (9시간 빠름)

    == 실제 웹의 데이터 처리 ==
    1) 저장 기준 시간대: UTC
    2) 화면 / 응답 시간대: KST(Asia/Seoul)로 변환해서 전달

    >> Spring Data JPA Auditing
        : @EnableJpaAuditing 활성화
        : 공통 부모 클래스 BaseTimeEntity에 @CreatedDate, @LastModifiedDate 사용
            - 엔티티에서 상속만하면 생성/ 수정 시간이 자동 관리

    cf) 필드명, 테이블명 - 시간, 날짜 데이터 명명
        > 날짜까지만 저장: 필드명Date
            EX) birthDate, weddingDate
        > 시간까지 저장 : 필드명At
            EX) createdAt, updatedAT

    cf) Gradle 의존성 추가
        > REST API(spring-boot-starter-web)
        > JPA+Auditing(spring-boot-starter-data-jpa)
        > MySQL 드라이버
        > Java 8 날짜 / 시간 직렬화 - jackson 드라이버
            : java 객채를 JSON 데이터로 변환(직렬화)하거나
                , JSON 데이터를 Java 객촐 변환하는데 사용
*/

public class R_Datetime {

}
