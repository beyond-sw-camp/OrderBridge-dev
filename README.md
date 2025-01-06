# Error Pirate
![brand](https://github.com/user-attachments/assets/ecd551ec-f43a-436a-a67c-7bc07794493c)

|![](https://avatars.githubusercontent.com/u/115945994)|![](https://avatars.githubusercontent.com/u/99578261)|![](https://avatars.githubusercontent.com/u/87793524)|![](https://avatars.githubusercontent.com/u/174118592)|![](https://avatars.githubusercontent.com/u/134343781)|![](https://avatars.githubusercontent.com/u/102805138)|
|:-:|:-:|:-:|:-:|:-:|:-:|
|[박희찬](https://github.com/hcbak)|[오민성](https://github.com/beanteacher)|[박지훈](https://github.com/dispear)|[이성민](https://github.com/sung-mini)|[남가람](https://github.com/catnine11)|[전아영](https://github.com/AYeong-Jeon)|

<br>

# Order Bridge
order bridge 프로젝트는 기내식 공급의 효율적인 관리를 목표로 한 공급망 관리 솔루션을 제공합니다. <br>
이 솔루션은 항공사 공급망 관리의 현대화와 사용자 편의성을 중심으로 설계되어 항공사의 경쟁력을 강화하고 운영 효율성을 높이는 데 기여합니다.
<br>

<br>

## 1. 프로젝트 기획

### 1-1. 프로젝트 기획서
[기획서](gif%2F%EA%B8%B0%ED%9A%8D%EC%84%9C.pdf)

### 1-2. 요구사항 명세서
<details>
  <summary>요구사항 명세서</summary>
  <div markdown="1">

[요구사항 명세서](https://docs.google.com/spreadsheets/d/e/2PACX-1vQxM_rIHvmLTbkV9LnaKnpo5b7QTwlGp9VqwIbPuwNH_rs0f3UpjXx70gHiFa4FGKJpYlsRoGB4w7w5/pubhtml?gid=594562128&single=true)

![image](https://github.com/user-attachments/assets/302f70d5-4497-4d7c-af3a-8910d614f1ea)
  </div>
</details>

### 1-3. 기능 명세서
[기능 명세서](https://docs.google.com/spreadsheets/d/e/2PACX-1vQxM_rIHvmLTbkV9LnaKnpo5b7QTwlGp9VqwIbPuwNH_rs0f3UpjXx70gHiFa4FGKJpYlsRoGB4w7w5/pubhtml?gid=213070244&single=true)

### 1-4. 시스템 아키텍처
<details>
  <summary>시스템 아키텍처</summary>
  <div markdown="1">

![image](https://github.com/user-attachments/assets/c0712a6f-9e98-487e-8f6f-c1ec3d18f794)

  </div>
</details>

### 1-5. WBS
[WBS](https://docs.google.com/spreadsheets/d/e/2PACX-1vQxM_rIHvmLTbkV9LnaKnpo5b7QTwlGp9VqwIbPuwNH_rs0f3UpjXx70gHiFa4FGKJpYlsRoGB4w7w5/pubhtml?gid=1397057023&single=true)

### 1-6. ERD
[ERD Cloud](https://www.erdcloud.com/d/KDXZgBYk6EeukmEa2)


### 1-7. 화면 설계서
[Figma](https://www.figma.com/design/75SMKjrV0RAYySz6nuMzhf/%EC%97%90%EB%9F%AC-%ED%95%B4%EC%A0%81%EB%8B%A8?node-id=0-1&t=DVRxg4B582bo3SLt-1)

<br>

## 2. 백엔드 설계 및 구축

### 2-1. 프로그램 사양서
|        구성 요소         |                      사양                       |
|:--------------------:|:---------------------------------------------:|
| CloudFront(Frontend) |                 Vue.js, Vite                  |
|  Elastic Beanstalk   | t3.large(2 vCPU, 8GB RAM), Nginx, Spring Boot |
|         RDS          |    db.t3.medium(2 vCPU, 4GB RAM), MariaDB     |
|          S3          |   5GB 표준 스토리지, 총 2개의 버킷(프론트엔드 프로젝트, 업로드 파일)   |

### 2-2. 단위 테스트 결과서

<br>

## 3. 프론트엔드 설계 및 구축

### 3-1. UI/UX 단위 테스트

<details>
  <summary>1. 로그인</summary>
    <img alt="로그인" src="/gif/01_로그인.gif">
</details>

<details>
  <summary>2. 창고</summary>
    <div>2-1. 창고 등록</div>
    <img alt="창고 등록" src="/gif/02_창고%20-%20등록.gif">
    <div>2-2. 창고 조회</div>
    <img alt="창고 조회" src="/gif/02_창고%20-%20조회.gif">
    <div>2-2. 창고 삭제</div>
    <img alt="창고 삭제" src="/gif/02_창고%20-%20삭제.gif">
</details>

<details>
  <summary>3. 거래처</summary>
    <div>3-1. 거래처 등록</div>
    <img alt="거래처 등록" src="/gif/03_거래처%20-%20등록.gif">
    <div>3-2. 거래처 조회</div>
    <img alt="거래처 조회" src="/gif/03_거래처%20-%20조회.gif">
    <div>3-2. 거래처 삭제</div>
    <img alt="거래처 삭제" src="/gif/03_거래처%20-%20삭제.gif">
</details>

<details>
  <summary>4. 품목</summary>
    <div>4-1. 품목 등록</div>
    <img alt="품목 등록" src="/gif/04_품목%20-%20등록.gif">
</details>

<details>
  <summary>5. 견적서</summary>
    <div>5-1. 견적서 등록</div>
    <img alt="견적서 등록" src="/gif/05_견적%20-%20등록.gif">
    <div>5-2. 견적서 조회, 엑셀다운로드</div>
    <img alt="견적서 조회" src="/gif/05_견적%20-%20조회,%20엑셀다운.gif">
    <div>5-2. 견적 현황 조회</div>
    <img alt="견적서 현황" src="/gif/05_견적%20-%20현황.gif">
</details>

<details>
  <summary>6. 주문서</summary>
    <div>6-1. 주문서 등록</div>
    <img alt="주문서 등록" src="/gif/06_주문%20-%20등록.gif">
</details>

<details>
  <summary>7. 발주서</summary>
    <div>7-1. 발주서 등록</div>
    <img alt="발주서 등록" src="/gif/07_발주%20-%20등록.gif">
    <div>7-2. 발주서 결재승인</div>
    <img alt="발주서 결재승인" src="/gif/07_발주%20-%20결재승인.gif">
</details>

<details>
  <summary>8. 구매서</summary>
    <div>8-1. 구매서 등록</div>
    <img alt="구매서 등록" src="/gif/08_구매%20-%20등록.gif">
    <div>8-2. 구매서 조회</div>
    <img alt="구매서 조회" src="/gif/09_구매%20-%20조회.gif">
    <div>8-3. 구매서 삭제</div>
    <img alt="구매서 삭제" src="/gif/09_구매%20-%20삭제.gif">
</details>

<details>
  <summary>9. 판매</summary>
    <div>9-1. 거래명세서 등록</div>
    <img alt="구매서 등록" src="/gif/09_판매%20-%20등록.gif">
</details>

<details>
  <summary>10. 작업지시서</summary>
    <div>10-1. 작업지시서 등록</div>
    <img alt="구매서 등록" src="/gif/10.작업지시서%20-%20등록.gif">
    <div>10-2. 작업지시서 수정</div>
    <img alt="작업지시서 수정" src="/gif/10.작업지시서%20-%20수정.gif">
    <div>10-3. 작업지시서 삭제</div>
    <img alt="작업지시서 삭제" src="/gif/10.작업지시서%20-%20삭제.gif">
</details>

<details>
  <summary>11. 생산불출</summary>
    <div>11-1. 생산불출 조회</div>
    <img alt="생산불출 등록" src="/gif/11.생산불출%20-%20검색.gif">
    <div>11-2. 생산불출 엑셀 다운로드</div>
    <img alt="생산불출 엑셀 다운로드" src="/gif/11.생산불출%20-%20엑셀.gif">
    <div>11-3. 생산불출 수정</div>
    <img alt="생산불출 수정" src="/gif/11.생산불출%20-%20수정.gif">
    <div>11-4. 생산불출 삭제</div>
    <img alt="생산불출 삭제" src="/gif/11.생산불출%20-%20삭제.gif">
</details>

<details>
  <summary>12. 생산입고</summary>
    <div>12-1. 생산입고 등록</div>
    <img alt="생산입고 등록" src="/gif/12_생산입고%20-%20등록.gif">
</details>

<details>
  <summary>13. 출하지시서</summary>
    <div>13-1. 출하지시서 등록</div>
    <img alt="출하지시서 등록" src="/gif/13_출하지시서%20-%20등록.gif">
    <div>13-2. 출하지시서 엑셀 다운로드</div>
    <img alt="출하지시서 엑셀 다운로드" src="/gif/13_출하지시서%20-%20엑셀,%20인쇄.gif">
    <div>13-3. 출하지시서 수정</div>
    <img alt="출하지시서 수정" src="/gif/13_출하지시서%20-%20수정.gif">
    <div>13-4. 출하지시서 삭제</div>
    <img alt="출하지시서 삭제" src="/gif/13_출하지시서%20-%20삭제.gif">
</details>

<details>
  <summary>14. 출하전표</summary>
    <div>14-1. 출하전표 등록</div>
    <img alt="출하전표 등록" src="/gif/14_출하전표%20-%20등록.gif">
    <div>14-2. 출하전표 엑셀 다운로드</div>
    <img alt="출하전표 엑셀 다운로드" src="/gif/14_출하전표%20-%20엑셀.gif">
    <div>14-3. 출하전표 인쇄</div>
    <img alt="출하전표 인쇄" src="/gif/14_출하전표%20-%20인쇄.gif">
</details>

<details>
  <summary>15. 챗봇</summary>
    <img alt="챗봇" src="/gif/15_챗봇.gif">
</details>

<details>
  <summary>16. 알림, 전자결재</summary>
    <div>16-1. 알림</div>
    <img alt="알림" src="/gif/16_알림%20-%20조회.gif">
    <div>16-2. 전자결재</div>
    <img alt="알림" src="/gif/16_결재.gif">
</details>

<br>

## 4. 시스템 통합

### 4-1. 통합 테스트 결과서

### 4-2. CI/CD 계획서 
[CI/CD 계획서](https://docs.google.com/spreadsheets/d/1gk9vqAHkigayGrVtWXJ4dcw1HV_7WgMIkAVy064mc1k/edit?gid=1079293076#gid=1079293076)
