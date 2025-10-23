# TravelLocal

## 대한민국의 관광 소도시를 여행하고 싶어 하는 외국인 관광객과, 숨은 매력을 공유하고 싶은 현지인을 연결하는 '현지인 기반 여행 정보 플랫폼'

### 개발배경:  
대한민국을 찾는 많은 외국인 관광객은 서울과 같은 대도시에 집중하고 있습니다. 그러나 최근 글로벌 여행 트렌드는 단순 관광을 넘어 '현지에서만 느낄 수 있는 로컬 경험'으로 이동하고 있습니다.
한국관광공사가 발표한 2025년 관광 트렌드 분석에서도 '로컬리즘 추구'가 핵심 키워드로 제시되었습니다. 이는 외국인 관광객이 대도시가 아닌 관광 소도시에서 한국의 현지를 느끼고자 하는 수요가 분명히 존재함을 보여줍니다.
하지만 기존 여행 플랫폼들은 대도시에만 집중되어 있어, 이러한 여행자들은 관광 소도시 방문을 원해도 정보 부족으로 인해 여행 계획 수립에 큰 어려움을 겪고 있습니다. TravelLocal은 바로 이 지점에서 시작합니다. 
상업적 추천에 지친 '현지 경험 중심 여행자'의 미충족 수요를 해결하고, 정보가 부족했던 숨겨진 소도시의 잠재력을 발굴하여경쟁이 치열한 레드오션이 아닌 가능성의 '블루오션' 시장에서 시작하고자 합니다.
이는 나아가 지방 관광산업 활성화를 촉진하는 원동력이 될 수 있습니다.

### 서비스 주 이용 대상:
광고성 정보를 싫어하며 숨겨진 현지 경험을 추구하는 외국인 여행자   
자신만의 경험을 공유하고 싶으며 수익창출을 원하는 관광 소도시 현지인

### 다운로드 링크
[![Download on OneStore](https://img.shields.io/badge/OneStore-Download-red?style=for-the-badge&logo=android)](https://m.onestore.co.kr/v2/ko-kr/app/0001002385)

---

## 기여자
| 이름  | Github 프로필     | 역할                |
|-----|------------------|-------------------|
| 김경탁 | [ryuseunghan](https://github.com/kkt9253) | Backend 팀장, Infra |
| 염준선 | [mjgwon24](https://github.com/yeomjunseon) | Backend           |
| 김민성 | [seocylucky](https://github.com/gitget1) | Frontend 팀장       |
| 황효동 | [HeoYeonGyu](https://github.com/gyehd0107) | AI, Frontend      |

---

## 서비스 아키텍쳐

<img width="800" alt="image" src="./assets/images/system_architecture.jpg" />


---

## 서비스 ERD

<img width="800" alt="image" src="./assets/images/erd.png" />

---

### Backend
- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- Oauth 2.0
- JWT
- WebSocket

### Database
- MySQL
- Redis

### Infra
- S3
- CloudFront
- Raspberry Pi 5 기반 On-Premise Server

---

## 주요 기능

| 기능 설명                                                                                                                                         |                            기능 아키텍쳐                            |
|:----------------------------------------------------------------------------------------------------------------------------------------------|:-------------------------------------------------------------:|
| <big><big>**AI 성향 테스트**</big></big> <br> 사용자는 간단한 문항을 통해 자신의 여행 성향을 확인할 수 있으며, AI가 이를 분석하여 개인의 성격과 취향에 맞는 여행 스타일을 제시합니다.                |     <img src="./assets/images/ai_mbti.jpg" width="400"/>      |
| <big><big>**GPS 로그 기반의 신뢰도 높은 리뷰**</big></big> <br> GPS 인증을 통해 실제 방문이 증명된 리뷰에 인증 마크를 부여하여 정보의 신뢰도를 높입니다. 허위 리뷰나 악의적인 평가를 쉽게 구분할 수 있습니다.                      |       <img src="./assets/images/gps.jpg" width="400"/>        |
| <big><big>**여러 플랫폼의 리뷰 통합 비교**</big></big> <br> 자사 리뷰뿐만 아니라 Google, Naver 등 여러 플랫폼의 평점과 리뷰를 한 화면에서 동시에 제공하여 객관적인 판단을 돕습니다.                                  | <img src="./assets/images/platforms_review.jpg" width="400"/> |
| <big><big>**현지인이 직접 만드는 여행 프로그램**</big></big> <br> 현지인이 직접 여행 코스를 등록하는 C2C 방식을 통해, 지역 주민만 아는 숨은 명소 등 생생한 여행 정보를 제공합니다.                                    |                           *아키텍쳐 X*                            |

---

## 서비스 흐름도

|                                       서비스 흐름도                                       |                                                    실행 영상                                                    |
|:-----------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------------:|
|   <img src="./assets/images/foreigner_1.png" width="800"/>   |                           <img src="./assets/videos/mbti-test.gif" width="180" />                           |
|  <img src="./assets/images/foreigner_2.png" width="800"/> |                         <img src="./assets/videos/find-program.gif" width="180" />                          |
|  <img src="./assets/images/foreigner_3.png" width="800"/> | <img src="./assets/videos/payment.gif" width="180" /><img src="./assets/videos/calendar.gif" width="180" /> |
|    <img src="./assets/images/foreigner_4.png" width="800"/>    |                          <img src="./assets/videos/GPS-review.gif" width="180" />                           |
|  <img src="./assets/images/korean_1.png" width="800"/> |                          <img src="./assets/videos/Program_out.gif" width="180" />                          |

---
