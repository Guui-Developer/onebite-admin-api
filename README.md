# OneBite Admin API

## 📖 프로젝트 개요

OneBite Admin API는 OneBite 서비스의 관리자 기능을 제공하는 API입니다. Spring Boot를 기반으로 AWS Lambda 및 API Gateway를 사용하여 서버리스 아키텍처로 구축되었습니다.

## 🛠️ 기술 스택

- **언어**: Java 21
- **프레임워크**: Spring Boot
- **빌드 도구**: Gradle
- **아키텍처**: AWS Serverless (Lambda, API Gateway)

## 📁 프로젝트 구조

```
.
├── src
│   ├── main
│   │   ├── java
│   │   │   └── dev
│   │   │       └── onebite
│   │   │           └── admin
│   │   │               ├── AdminApplication.java
│   │   │               ├── application
│   │   │               ├── domain
│   │   │               ├── infra
│   │   │               └── persentation
│   │   │                   ├── api
│   │   │                   │   └── v1
│   │   │                   │       ├── AdminAuthController.java
│   │   │                   │       ├── CategoryController.java
│   │   │                   │       ├── CategoryGroupController.java
│   │   │                   │       └── ContentController.java
│   │   │                   ├── dto
│   │   │                   └── exception
│   │   └── resources
│   └── test
├── build.gradle.kts
├── template.yaml
└── README.md
```

- **`application`**: 애플리케이션의 비즈니스 로직을 담당하는 서비스 계층입니다.
- **`domain`**: 도메인 모델과 관련된 클래스를 포함합니다.
- **`infra`**: 데이터베이스, 외부 API 연동 등 인프라 관련 코드를 포함합니다.
- **`persentation`**: API 엔드포인트를 정의하는 컨트롤러와 DTO를 포함합니다.

## 🚀 API 엔드포인트

### 인증

- `POST /login`
  - 관리자 로그인을 처리합니다.

### 카테고리

- `GET /categories`
  - 모든 카테고리를 페이지네이션하여 조회합니다. `keyword`로 검색이 가능합니다.
- `POST /categories`
  - 새로운 카테고리를 생성합니다.
- `PUT /categories/{categoryId}`
  - 기존 카테고리를 수정합니다.
- `DELETE /categories`
  - 하나 이상의 카테고리를 삭제합니다.

### 카테고리 그룹

- `GET /group`
  - 모든 카테고리 그룹을 페이지네이션하여 조회합니다. `keyword`로 검색이 가능합니다.
- `POST /group`
  - 새로운 카테고리 그룹을 생성합니다.
- `PUT /group/{groupId}`
  - 기존 카테고리 그룹을 수정합니다.
- `DELETE /group`
  - 하나 이상의 카테고리 그룹을 삭제합니다.

### 콘텐츠

- `GET /content`
  - 모든 콘텐츠를 페이지네이션하여 조회합니다. `keyword`로 검색이 가능합니다.
- `POST /content`
  - 새로운 콘텐츠를 생성합니다.
- `PUT /content/{contentId}`
  - 기존 콘텐츠를 수정합니다.
- `DELETE /content`
  - 하나 이상의 콘텐츠를 삭제합니다.

## ❗ 에러 코드 정의

| Error Code              | HTTP Status | Description                        |
| ----------------------- | ----------- | ---------------------------------- |
| `VALIDATION_ERROR`      | 400         | 필수 필드가 누락되었습니다.        |
| `DUPLICATED_CODE`       | 400         | 중복된 필드입니다.                 |
| `INVALID_CODE_LENGTH`   | 400         | 올바르지 않는 코드 길이입니다.     |
| `CATEGORY_HAS_CONTENT`  | 400         | 하위 데이터가 존재하여 삭제할 수 없습니다. |
| `ADMIN_LOGIN_FAIL`      | 401         | 아이디 또는 패스워드가 틀렸습니다. |
| `CODE_NOT_FOUND`        | 404         | 존재하지 않는 카테고리 그룹입니다. |
| `ID_NOT_FOUND`          | 404         | 올바르지 않는 아이디입니다.        |
| `DELETE_DATA_NOT_FOUND` | 404         | 삭제할 대상이 존재하지 않습니다.   |
| `CONTENT_NOT_FOUND`     | 404         | 존재하지 않는 컨텐츠입니다.        |
| `AWS_ERROR`             | 500         | AWS 관련 오류가 발생했습니다.      |

