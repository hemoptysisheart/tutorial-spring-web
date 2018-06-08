# CH.10 - 인증

회원가입(계정 등록)은 만들었다.
인증을 추가해 로그인 기반의 웹 애플리케이션으로 전환한다.

> * 인증(Authentication) : 로그인.
> * 인가(Authorize) : 현재 유저가 어떤 유저인가. 게스트/일반/관리자 등.

## STEP 1 - 시큐리티 모듈

먼저 Spring Security 모듈을 추가한다.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```
[pom.xml](../../pom.xml)