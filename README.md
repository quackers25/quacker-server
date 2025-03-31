# 🐣 Quackers

트위터 클론 프로젝트 **Quacker**로 빌드 전환, CI/CD 구축, Spring Boot 최신화, 테스트 및 API 명세화를 통해 안정성과 확장성을 강화한 프로젝트입니다.

![logo-320-320-none](https://github.com/user-attachments/assets/7ccbc32e-faf7-4449-8d44-1443efbadf67)

---

## 👥 팀원

| 팀장 | 팀원 | 팀원 | 팀원 |
|:---:|:---:|:---:|:---:|
| [**이한결**](https://github.com/hangyeoli) | [**김형준**](https://github.com/kimnoca) | [**우상진**](https://github.com/SangJin521) | [**정신우**](https://github.com/cupokki) |

---

## 🎯 프로젝트 목표

- SNS(Twitter) 클론 구현
- 기존 프로젝트 마이그레이션 경험
- Git Flow 브랜치 전략 사용
- Spring Boot 2 → 3.4.3 업그레이드
- 기능 개선 및 디버깅
- 성능 최적화 & 트러블슈팅
- 문제 해결 프로세스 학습
- API 명세화
- CI/CD 파이프라인 구축

---

## 📁 프로젝트 산출물
- **팀 노션 페이지**
  - [팀 노션페이지](https://www.notion.so/goormkdx/Quackers-1b3c0ff4ce31808fa50aeebfd0f8479b?pvs=4)
- **기획안**
  - [Quacker 기획안.pdf](https://github.com/user-attachments/files/19532828/Quacker.pdf)
    
- **요구사항 명세서**
  - [요구사항정의서_엑셀버전](https://docs.google.com/spreadsheets/d/1TPSz_DULZmGht2Iw3baB9D3BaSDIYtUndCeCAVwxVIY/edit?gid=1675381079#gid=1675381079)
    
- **API 명세서**
  - 배포하여 swagger-ui 참조
  - [변환된 yaml 파일](https://github.com/quackers25/quacker-server/blob/dev/api.yaml)
  
- **ER 다이어그램**  
  ![image](https://github.com/user-attachments/assets/db486224-6888-425c-9fc7-6f1cf40e734d)

- **아키텍처 다이어그램**  
  ![image](https://github.com/user-attachments/assets/26370cfc-f89c-48c1-8fbb-69a3d8afc95f)

- **사용 기술 스택**
  - **Backend**  
    ![Java](https://img.shields.io/badge/JAVA-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
    ![Spring Boot](https://img.shields.io/badge/SPRING%20BOOT-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
    ![Spring Security](https://img.shields.io/badge/SPRING%20SECURITY-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)

  - **Infra**  
    ![MySQL](https://img.shields.io/badge/MYSQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
    ![Amazon S3](https://img.shields.io/badge/AMAZON%20S3-569A31?style=for-the-badge&logo=amazon-s3&logoColor=white)
    ![Amazon EC2](https://img.shields.io/badge/AMAZON%20EC2-FF9900?style=for-the-badge&logo=amazon-ec2&logoColor=white)
    ![AWS](https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=white)

  - **협업 툴**  
    ![Discord](https://img.shields.io/badge/DISCORD-5865F2?style=for-the-badge&logo=discord&logoColor=white)
    ![Notion](https://img.shields.io/badge/NOTION-000000?style=for-the-badge&logo=notion&logoColor=white)
    ![Jira](https://img.shields.io/badge/JIRA-0052CC?style=for-the-badge&logo=jira&logoColor=white)
    ![Git](https://img.shields.io/badge/GIT-F05032?style=for-the-badge&logo=git&logoColor=white)
    ![GitHub](https://img.shields.io/badge/GITHUB-181717?style=for-the-badge&logo=github&logoColor=white)

- **최종 발표자료**
  - [Quacker 최종 발표자료.pdf](https://github.com/user-attachments/files/19532850/Quacker.pdf)

---
## 🤔 아쉬운 점 & 개선 방향
  - 단위 테스트 코드 미비
  - FE 미연동
  - RDS 미적용

## 📚 수행한 작업 & 배운 점

### 🧠 정신우
- **Spring Security 구현**
  - [주요 클래스 정리](https://www.notion.so/Spring-Security-1bdbed0e666780d8b15bd765d046b050?pvs=21)
  - [JWT 로그인 방식](https://www.notion.so/Spring-Security-JWT-1babed0e666780a8ae65cdcb9bd3bada?pvs=21)
- **JWT 토큰 및 인증 처리**
  - [JWT란?](https://www.notion.so/JWT-Json-Web-Token-19ebed0e6667800995a9c25d3c8d4838?pvs=21)
  - [Refresh Token](https://www.notion.so/Refresh-Token-JWT-1bcbed0e666780a380efe93f9b1b7d41?pvs=21)
  - [JWT Blacklist 관리](https://www.notion.so/JWT-Blacklist-1bcbed0e6667802487b1f612658f28dc?pvs=21)
- **테스트 프레임워크**
  - [Unit Test](https://www.notion.so/Unit-Test-160bed0e666780ae8e80d751c230dd47?pvs=21)
- **Java Bean Validation**
  - [관련 개념 정리](https://www.notion.so/Spring-Java-Bean-Validation-1bbbed0e666780e29a0ffa26f62d9e69?pvs=21)

> RESTful 설계, SpEL 이해의 중요성 등 여러 기술적 고민을 함께 경험

---

### 🧠 이한결
- JPA 양방향 매핑 및 동기화
- 트랜잭션 관리 & 동시성 제어
- N+1 문제 및 메모리 누수 해결
- RESTful API 설계 & 버전 관리
- 도메인 중심 설계 & 모듈화
- 테스트 코드 작성 및 리팩토링 경험

---

### 🧠 김형준
- **Mockito 단위 테스트 작성 학습**
- Mock 객체의 한계 및 사용 범위에 대한 고민
- "테스트하기 좋은 코드"의 중요성 인식
> 다음 프로젝트에서는 테스트 코드 중심의 구조 설계를 목표로

---

### 🧠 우상진
- 전체적인 프로젝트 흐름 이해
- Post & PostImage 구현 과정에서 DTO와 Entity 역할 분담 학습
- 성능 개선을 위한 고민
- 테스트 코드 작성법에 대한 관심과 학습

---

## 💬 짧은 후기

- **정신우** : 테스트 코드를 병행하면서 개발하는 중요성을 깨달았습니다. 매일 피드백을 받으며 많이 배웠습니다.
- **이한결** : 스크럼과 협업을 통한 빠른 개발 경험이 좋았고, 테스트 프레임워크 사용도 유익했습니다.
- **김형준** : 매일 회의와 빠른 사이클 개발이 인상 깊었습니다.
- **우상진** : 테스트 코드에 대한 첫 도전이었고, 더 익숙해지고 싶습니다. 많은 것을 배울 수 있었던 좋은 경험이었습니다.
