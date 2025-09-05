package com.example.k5_iot_springboot.이론;

/*
* === 깃 프랜치 전략 ===
* 1) 메인 브렌치 보호: main
* 2) 개발 과정 통합 브랜치: develop
* 3) 작은 단위 기능 브랜치: 패턴[type/scope-short-desc]
*
* - 작업 흐름
*   : 이슈 생성 -> 기능 브랜치 생성 -> 커밋/푸시(로컬)
*       -> PR(Pull Request) 생성 -> 리뷰/체크 통과 -> develop에 merge
*
* === 브랜치 네이밍 규칙 ===
* [패턴]: type/scope-short-desc
*
* 1) type: feature | fix | refactor | chore | docs | test
*       feature - 새로운 기능 추가 (실질적인 개발)
*       fix - 버그 수정
*       refactor - 기능 변경 없는 코드 리팩토링
*
*       chore - 빌드 업무 및 "설정 변경"
*       docs - 문서 수정 및 추가 (README.md 수정)
*       test - 테스트 코드 관련 작업
*
* 2) scope: user | auth | task | project | infra | api (필요 시 추가 가능)
*       user - 사용자 정보
*       auth - 인증/권한
*       task - 할 일 정보
*       project - 할 일 범주 관리
*       ...
*
*       infra - 서버, 데이터베이스, 네트워크 등 서비스 기반이 되는 인프라 접근/관리
*       pai - API 자체에 대한 접근 권한이나 API 호출에 필요한 범위 지정
*
* 3) short-desc: 영문 소문자만 사용, 숫자 사용 가능, 기호는 하이픈만(-)
*
* [ 예시 ]
* feature/task-create
* fix/auth-token-expiry
* refactor/project-service-layer
* feature/task-search
*
* == 브랜치 로컬 생성 + 원격 연결 ==
* 1) 새 브랜치 연결 및 전환
* git checkout -b feature/task-create-deadline
*
* 2) 원격 연결 (최초 1회만: -u)
* git push -u origin feature/task-create-deadline
*
* 3) 이후에는 간단하게
*   git push
*   git pull 만으로 가능
* */
public class _branch_naming {
}
