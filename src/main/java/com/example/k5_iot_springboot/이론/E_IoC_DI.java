package com.example.k5_iot_springboot.이론;

/*
    === 제어의 역전(IoC) VS 의존성 주입(DI) ===

    1. 제어의 역전 (Inversion of Control)
    : 프로그램의 제어 흐름을 개발자가 직접 통제하지 않고
        , 외부 컨테이너(스프링 컨테이너)에 위임하는 방식
    - 제어의 권한이 컨테이너에게 있어 객체의 생명 주기를 컨테이너가 관리
    - IoC 자체는 개념(이론), 이를 구현하는 대표적인 방법 DI(의존성 주입)

    2. 의존성 주입 (Dependency Injection)
    : 클래스가 필요로 하는 객체(의존성, Dependency)를 외부에서 주입(Injection) 받아 사용하는 방식
    - 객체 간 결합도 감소, 유연성과 재사용성 증가 & 확장성, 유지보수성, 테스트 용이성 향상
    >> 생성자 주입(권장), 필드(Field) 주입, 세터(Setter) 주입
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

// 1. 전통적인 자바 프로그래밍 방식
class Book1 {
    private String title;

    public Book1(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}

class BookStore1 {
    private Book1 book;

    public BookStore1() {
        // Book1 객체를 생성자가 직접 인스턴스화
        // : 개발자가 객체 생성과 관리(수명 주기)를 모두 책임

        // >> Book1이 변경되면 BookStore1도 같이 수정 (결합도 높다)
        //      : 확장성, 유지보수성, 테스트에 어려움
        this.book = new Book1("Spring Boot 기초");
    }

    public void displayBook() {
        System.out.println("Book: " + book.getTitle());
    }
}

// 2. 스프링 제어의 역전 프로그래밍 방식
@Component
// : 스프링 컨테이너가 해당 객체를 관리하도록 설정
// >> 스프링 빈(Bean)
// >> 스프링 컨테이너에 의해 관리되는 재사용 가능한 소프트웨어 컴포넌트
class Book2 {
    private String title;

    public Book2() {
        this.title = "스프링 기초";
    }

    public String getTitle() {
        return this.title;
    }
}

@Component
class BookStore2 {
    private Book2 book;

    // 스프링이 Book2 객체를 생성하여 자동으로 BookStore2에 넣어줌 (매개변수로 전달)
    // - 개발자가 new 연산자 사용 없이, 스프링 컨테이너가 객체를 직접 만들어서 '주입'
    @Autowired // 스프링 컨테이너에게 해당 타입의 밴을 찾아 주입하는 어노테이션
    public BookStore2(Book2 book) {
        this.book = book;
    }

    public void displayBook() {
        System.out.println("Book: " + book.getTitle());
    }
}

// 1. 스프링 컨테이너
// : 애플리케이션 내에서 객체(빈, Bean)의 생명 주기와 설정을 관리
// - 제어의 역전을 실현, 의존성 주입을 지원

// 2. 빈(Bean)
// : 스프링 컨테이너가 관리하는 객체
// - 등록 방식
//      > 컴포넌트 스캔 기반 @Component (클래스 단위 등록)
//          + 특화 어노테이션 @Controller, @Service, @Repository 등
//              : 내부에 @Component 포함
//      > 자바 설정 기반 @Configuration 클래스 안에 @Bean 메서드로 수동 등록 (메서드 단위 등록)

// - 네이밍 규칙
//      > 클래스명의 첫 글자를 소문자로 바꾼 이름이 기본 빈 이름
//      EX) Book -> book, BookStore -> bookStore 등
//          이름 설정 @Component("bookSpecial")

//@Configuration
class ExampleConfig {
//    @Bean
    public void ExampleBean() {
        System.out.println("예시 빈 등록");
    }
}

/*
    === 의존성 주입 방식 (생성자, 필드, 세터) ===
    1) 생성자 주입 방식 (권장)
        - 불변 보장 (final 가능)
        - 순환 참조 조기 감지
        - 테스트 용이성
        - 필수 의존성 보장 (주입 없이는 인스턴스 생성 불가)

    2) 필드 주입 방식
        - 테스트 어려움
        - 순환 참조 숨김
        - 불변 보장 불가

    3) 세터 주입 방식
        - 불변성 약화, 객체가 불완전 상태로 생성될 가능성 존재
*/
public class E_IoC_DI {
}