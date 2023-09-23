# rego
여행 계획 및 항공,호텔 예약

<div align=center><h1>⚙️ Environment</h1></div>

<div align=center> 
  <img src="https://img.shields.io/badge/intellij_idea-000000?style=for-the-badge&logo=intellijidea&logoColor=white"> 
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
  <img src="https://img.shields.io/badge/oracle_cloud-F80000?style=for-the-badge&logo=oracle&logoColor=white"> 
<br>

  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<img src="https://img.shields.io/badge/spring_securiety-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
<br>

  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
  <img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> 
  <img src="https://img.shields.io/badge/thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white">
  <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white"> 
  <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> 
   <br>

  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <br>

<div align=center><h1>✈️ API</h1></div>
  <img src="https://img.shields.io/badge/kakao-FFCD00?style=for-the-badge&logo=kakao&logoColor=white">
  <img src="https://img.shields.io/badge/naver-03C75A?style=for-the-badge&logo=naver&logoColor=white">
  <img src="https://img.shields.io/badge/google-4285F4?style=for-the-badge&logo=google&logoColor=white">
  <img src="https://img.shields.io/badge/google_maps-4285F4?style=for-the-badge&logo=googlemaps&logoColor=white">

</div>


```mermaid
flowchart TD
    A[메인페이지] --> B[로그인 페이지]
    B --> C[API 로그인]
    C --> |정보 O| D[로그인]
    C --> |정보 X| E[회원가입 페이지]
    A --> F[항공권 조회]
    E --> |가입 완료| B
    F --> |API| G[항공조회 정보]
    A --> I
    G --> |항공권선택| H[예약 페이지]
    G --> |플래너작성| I[플래너 작성페이지]
    I --> |임시저장| A
    I --> |작성완료| J[플래너 상세페이지]
    I --> |항공예약| G
    A --> K[추천플래너 목록]
    K --> |플래너 선택| J
    J --> |플래너 복사하기| I
    A --> L[관리자 페이지]
    L --> M[회원 관리]
    L --> N[예약 내역-전체]
    A --> O[마이페이지]
    O --> P[예약내역]
    O --> R[회원정보수정]
    A --> Q[고객센터]
    H --> S[결제하기]
    S --> |플래너 작성| I
    O --> |찜,내 플래너 클릭| J
    L --> Q
    K --> T[관광지 목록]
    T --> U[관광지 상세보기]
    L --> V[관광지 등록하기]
    V --> U
```
