# CH.XX - Git 히스토리 정리

## STEP 1 - 커밋 네트워크의 교차 현상

### 현상

프로젝트 저장소에 다음 순서로 작업이 발생했다.

1. `master` 브랜치가 c02b5479d862eabb3bc1ce0157f36d1c888f75b4 커밋일 때
[CH.09 - 안정성 개선](../09/readme.md)의 `ch9_enhance_stablility` 브랜치가 작업을 시작.
1. `master` 브랜치가 동일하게 c02b5479d862eabb3bc1ce0157f36d1c888f75b4 커밋일 때
수정사항 [`CH1`](/hellosamuel/tutorial-spring-web/tree/CH1)이 발생.
1. [`CH1`](/hellosamuel/tutorial-spring-web/tree/CH1)에서 [Pull Request #1](/hemoptysisheart/tutorial-spring-web/pull/1)가 생성.
1. [CH.09 - 안정성 개선](../09/readme.md)의 `ch9_enhance_stablility` 브랜치가 작업을 끝내고 저장소에 반영.
1. [Pull Request #1](/hemoptysisheart/tutorial-spring-web/pull/1)가 병합.

그 결과 Git 커밋 히스토리에 교차 현상(L.5 ~ 6)이 나타난다.

```
00 *   c420f27 - (HEAD -> chXX_cleanup_git_history, origin/master, origin/HEAD) Merge pull request #1 from hemoptysisheart/hellosamuel/CH1 (3 weeks ago) <H2>
01 |\
02 | * 3c03cac - (origin/hellosamuel/CH1) (EDIT) 문서 오탈자수정. (4 weeks ago) <Lee Samuel>
03 * |   4ed8393 - Merge branch 'ch9_enhance_stability' (3 weeks ago) <hemoptysisheart>
04 |\ \
05 | |/
06 |/|
07 | * 7a4b947 - (ADD) C9S3 - 모니터링 로그 (3 weeks ago) <hemoptysisheart>
08 | * d7f1660 - (ADD) C9S2 - 디버깅 로그 (4 weeks ago) <hemoptysisheart>
09 | * 063f71d - (ADD) C9S1 - 코드 공통화 : 폼 페이지 출력 (4 weeks ago) <hemoptysisheart>
10 |/
11 *   c02b547 - Merge branch 'ch8_enhance_project_structure' (4 weeks ago) <hemoptysisheart>
```
![교차하는 커밋 네트워크](step_1_crossing_commit_network.png)

커맨드라인에서 확인하는 커밋 네트워크는 알아보기가 좀 힘들다. 깃허브에서 제공하는 [커밋 네트워크](/hemoptysisheart/tutorial-spring-web/network)에서 확인하면,

![풀 리퀘스트를 병합하기 전의 커밋 히스토리](step_1_before_merge_pull_request.png)

풀리퀘스트를 병합하기 전에 하나의 커밋에서 두 브랜치가 가지를 쳐서 나오고, 그 중 하나가 먼저 작업을 끝내고 `master` 브랜치에 병합된 것을 확인할 수 있다.

그리고 풀리퀘스트를 그대로 병합하면

![풀 리퀘스트를 병함한 후의 커밋 히스토리](step_1_after_merge_pull_request.png)

이렇게 커밋 네트워크가 교차한다.
커밋 네트워크를 그리는 방식에 따라선 교차하지 않도록 만들 수도 있긴 하지만, 커밋 네트워크의 폭은 3가닥으로, 줄어들지 않는다.

### 문제점

1. 커밋 히스토리의 교차 현상은 현재의 브랜치/커밋의 **변경이력을 읽기 어렵게 만드는 주요 원인**이다.
1. 커밋 네트워크에서 동시에 그려지는 가닥은 **동시에 고려해야 하는 범위**이다.
1. 동시에 과거의 커밋을 기준으로 작성한 변경은 **충돌의 주요 원인**이다.

### 방지 원칙

1. 작업은 기준 브랜치(이 경우 `master`)의 현재 커밋에서 시작한다.
1. 기준 브랜치의 현재 커밋을 가지지 않은 작업은 병합하지 않는다.

## STEP 2 - 교차하지 않도록 정리하기

마침 이대로 병합하면 네트워크가 교차하고, 네트워크를 그리는 순서를 바꾼다 해도 교차를 없앨 수 없는 브랜치가 있다. 바로 03번 줄에 있는 bb2f62f5f67f90ccb68330fdaad77c9004e2a394 커밋을 가진 `ch10_authentication` 브랜치이다.

1. 4ed8393bb9bd11ccf2d73529ee516a2ba246657e 커밋에서 `ch10_authentication` 브랜치를 작성, 작업 시작.
1. [`CH1`](/hellosamuel/tutorial-spring-web/tree/CH1)에서 [Pull Request #1](/hemoptysisheart/tutorial-spring-web/pull/1)가 생성.
1. [Pull Request #1](/hemoptysisheart/tutorial-spring-web/pull/1)이 `master` 브랜치에 병합.
1. `ch10_authentication` 브랜치가 작업을 끝내고 d09aa5a604363a025c072ed039654cf6181fbc7b 커밋에서 병합해야 하는 상태가 됨.

```
00 * d09aa5a - (ch10_authentication) (FIX) 마크다운 오류 수정. (4 minutes ago) <hemoptysisheart>
01 * bb2f62f - (FIX) 템플릿 로딩 에러에 대응. (13 days ago) <hemoptysisheart>
02 * d803ea7 - (ADD) C10S2 - 로그인 상태에 따른 UI 선택 (13 days ago) <hemoptysisheart>
03 * 302fdd2 - (ADD) C10S1R4 - 애플리케이션 설정 (13 days ago) <hemoptysisheart>
04 * 74c31dd - (ADD) C10S1R3 - 인증용 계정 정보 로더 (3 weeks ago) <hemoptysisheart>
05 * ac2c133 - (ADD) C10S1R2 - 웹 세션의 유저 정보 저장용 타입 정의 (4 weeks ago) <hemoptysisheart>
06 * 3b24968 - (ADD) C10S1R1 - 보안 모듈 추가 (5 weeks ago) <hemoptysisheart>
07 * e6dc2ab - (ADD) C10 - 인증 (5 weeks ago) <hemoptysisheart>
08 | * 97c445c - (HEAD -> chXX_cleanup_git_history) (ADD) C?S2R1 - 커밋 네트워크 고치기 샘플 (14 minutes ago) <hemoptysisheart>
09 | * db7a8f5 - (ADD) CH.XX - 교차하는 커밋 네트워크 이미지. (79 minutes ago) <hemoptysisheart>
10 | * 4fb0430 - (ADD) CH.XX - 커밋 히스토리의 교차 현상과 문제점 (13 days ago) <hemoptysisheart>
11 | *   c420f27 - (origin/master, origin/HEAD, master) Merge pull request #1 from hemoptysisheart/hellosamuel/CH1 (4 weeks ago) <H2>
12 | |\
13 |/ /
14 | * 3c03cac - (origin/hellosamuel/CH1) (EDIT) 문서 오탈자수정. (6 weeks ago) <Lee Samuel>
15 * |   4ed8393 - Merge branch 'ch9_enhance_stability' (5 weeks ago) <hemoptysisheart>
```
![그대로 병합하면 꼬이는 커밋 네트워크 상태](step_2_before_refine.png)