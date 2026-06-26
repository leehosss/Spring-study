# Spring Boot 게시판

Spring Boot + Spring Security + JPA + MySQL 로 구현한 로그인 기능이 포함된 게시판 프로젝트입니다.

## 기술 스택

| 분류 | 기술 |
|------|------|
| Backend | Java 17, Spring Boot 3.5 |
| Security | Spring Security 6, BCrypt |
| ORM | Spring Data JPA, Hibernate |
| Database | MySQL 8 |
| Template | Thymeleaf |
| UI | Bootstrap 5 |
| Build | Maven |

## 주요 기능

- 회원가입 / 로그인 / 로그아웃
- 게시글 목록 조회 (페이징)
- 게시글 작성 / 수정 / 삭제 (로그인 필요)
- 조회수 자동 증가
- 작성자 본인만 수정/삭제 가능

## 프로젝트 구조

```
src/main/java/com/example/springstudy/
├── config/
│   └── SecurityConfig.java       # Spring Security 설정
├── controller/
│   ├── HomeController.java       # / → /posts 리다이렉트
│   ├── AuthController.java       # 로그인, 회원가입
│   └── PostController.java       # 게시글 CRUD
├── service/
│   ├── UserService.java          # 회원 로직, UserDetailsService 구현
│   └── PostService.java          # 게시글 로직
├── repository/
│   ├── UserRepository.java
│   └── PostRepository.java
├── entity/
│   ├── User.java                 # users 테이블 매핑
│   └── Post.java                 # posts 테이블 매핑
└── dto/
    ├── RegisterForm.java          # 회원가입 요청 데이터
    └── PostForm.java              # 게시글 작성/수정 요청 데이터
```

## 실행 방법

### 1. MySQL DB 생성

```sql
CREATE DATABASE spring_board DEFAULT CHARACTER SET utf8mb4;
```

### 2. application.properties 설정

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/spring_board?useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Seoul
spring.datasource.username=root
spring.datasource.password=본인_비밀번호
```

### 3. 실행

```bash
./mvnw spring-boot:run
```

브라우저에서 `http://localhost:8080` 접속

> 테이블은 JPA가 자동으로 생성합니다 (`ddl-auto=update`)

## 아키텍처 흐름

```
클라이언트 요청
    ↓
SecurityFilterChain (권한 체크)
    ↓
Controller (요청 매핑, DTO로 데이터 수신)
    ↓
Service (비즈니스 로직, 트랜잭션)
    ↓
Repository (CRUD)
    ↓
DB (MySQL) → Entity 변환
    ↓
Controller → Model → Thymeleaf → HTML 응답
```

## 테이블 구조

```
users                           posts
┌─────────────────┐            ┌──────────────────────────┐
│ id (PK)         │◄──────────│ id (PK)                  │
│ username        │            │ title                    │
│ password        │            │ content (TEXT)           │
│ nickname        │            │ view_count               │
│ created_at      │            │ user_id (FK)             │
└─────────────────┘            │ created_at               │
                               │ updated_at               │
                               └──────────────────────────┘
```
