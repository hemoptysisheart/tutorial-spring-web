# CH.08 - 프로젝트 구조 개선하기

## STEP 1 - 컴포넌트 레이어에 따라 패키지 분리하기

소스코드의 기능에 따라 패키지를 나누는 것은 각 타입의 의존성 관리를 기능 분리 등이 가능하게 만드는 기본 조치이다.
우선 각 컴포넌트 레이어에 맞춰 패키지를 나눈다.

```
./src/main
├── java
│   └── hemoptysisheart
│       └── github
│           └── com
│               └── tutorial
│                   └── spring
│                       └── web
│                           ├── borderline
│                           │   ├── AccountBorderline.java
│                           │   ├── AccountPo.java
│                           │   └── CreateAccountCmd.java
│                           ├── configuration
│                           │   └── JpaConfiguration.java
│                           ├── controller
│                           │   ├── RootController.java
│                           │   └── SignUpReq.java
│                           ├── dao
│                           │   └── AccountDao.java
│                           ├── jpa
│                           │   ├── entity
│                           │   │   └── AccountEntity.java
│                           │   └── repository
│                           │       └── AccountRepository.java
│                           ├── runner
│                           │   └── ApplicationRunner.java
│                           └── service
│                               ├── AccountService.java
│                               └── CreateAccountParams.java
└── resources
    ├── application.yml
    └── templates
        └── _
            ├── index.html
            ├── newbie.html
            └── signup.html
```
[전체 구조](step_1_tree.txt)

* `hemoptysisheart.github.com.tutorial.spring.web.jpa`
: JPA Entity와 JpaRepository는 JPA에 종속적인 타입이기 때문에 `jpa.entity`와 `jpa.repository` 패키지로 분리한다.
* `hemoptysisheart.github.com.tutorial.spring.web.runner`
: 패키지 중복을 피하기 위해 클래스가 하나뿐이지만 별도의 패키지로 분리.