# 성능 저하 감지가 어려운 이유
- 서비스 성능 저하는 간헐적으로 시작되어 확산될 수 있음
	- 첫 징후는 몇몇 요청이 느려지는 정도일 수 있음
- 원격 서비스 호출은 대개 동기식이며 장기간 수행되는 호출을 중단하지 않음
	- 대부분의 원격 서비스 호출 프로토콜이 동기식이기 때문에 서비스를 호출하고 기다리는 경우가 많음
- 대개 원격 자원의 부분적인 저하가 아닌 완전한 실패를 처리하도록 애플리케이션을 설계함
	- 서비스가 완전히 요청 처리에 실패하지 않는 한 지속적으로 불량한 서비스를 호출하고 빠르게 실패하는 방향을 선택하지 못하는 경우가 많음
	- 호출하는 서비스는 정상 서비스를 유지하며 성능만 저하될 수도 있으나, 리소스 부족 상태로 빠질 수 있음

# 클라이언트 측 회복성
## 클라이언트 측 로드 밸런싱
- 로드밸런싱은 호출할 서비스 인스턴스의 물리적 위치를 캐싱하는 작업을 포함함
- 로드밸런서가 문제를 탐지하면 가용 서비스 풀에서 문제된 서비스 인스턴스를 제거

## 서킷브레이커
- 전기 회로의 차단기 모델링
- 호출이 너무 오래 걸리면 서킷브레이커가 개입해서 호출을 종료
- 원격 호출에 대한 모든 호출 모니터링
- 고장난 원격 리소스 호출을 방지함

## 폴백 처리
- 원격 서비스 호출이 실패할 때 예외를 생성하지 않고 대체 코드를 수행하는 것
- 기본값을 반환하는 형태로 사용할 수 있음

## 벌크헤드
- 원격 리소스에 대한 호출을 리소스 별로 쓰레드 풀 분리
- 한 서비스가 느리게 응답하더라도, 다른 서비스는 영향을 받지 않게 됨

## 중요한, 이득인 이유
- 빠른 실패 : 빠르게 요청을 실패하기 때문에 전체 애플리케이션이 다운되는 리소스 고갈 이슈를 방지
- 원만한 실패 : 서킷브레이커는 요청을 실패시키거나, 사용자의 의도를 충족하는 대체 메커니즘을 제공할 수 있게 함
- 원활한 회복 : 서킷브레이커 패턴이 중개자 역할을 하므로, 사람의 개입 없이 리소스에 디한 재접근을 허용하도록 주기적으로 확인함

# Resillience4j
## 제공 기능
- 서킷브레이커
- 재시도
- 벌크헤드
- 속도 제한(Rate Limit)
- 폴백 처리

## 서킷브레이커
- 기존 버전에서는 링 비트 버퍼 사용
	- Closed, Half-Open 상태에서 사용하는 링 버퍼가 다름
	- 일정 비율 이상 실패하게 되면 서킷브레이커가 발동하여 서킷이 열림
- 1.0.0 버전부터 슬라이딩 윈도우로 변경됨
	- count based
	- time based

## 폴백 처리
- 메서드를 지시하여 폴백 처리를 할 수 있음

## 벌크헤드
- 2가지 종류의 벌크헤드를 지원함
	- 세마포어 벌크헤드 : 세마포어의 동시성 이상의 요청이 들어오면 요청을 거부함
	- 스레드 풀 벌크헤드 : 제한된 큐와 고정 사이즈 스레드 풀 사용, 풀의 스레드를 모두 사용하고 큐가 터지면 요청 거부함
- 사용하기 위해서는 CompletableFuture를 반환해야 한다고 해서 사용하지 못했음
- 스레드 풀 계산법
```
(서비스가 정상을 유지하는 피크 TPS * P99 레이턴시) + 부하 대비 추가 스레드
```

## 재시도 패턴
- 최신 버전 기준 설정이 `maxRetryAttempts` -> `maxAttempts` 로 변경

## 속도 제한 패턴
- 다음의 제한 메커니즘 지원
	- SemaphoreBasedRateLimiter : 최대 허용 스레드 수를 지정함
	- AtomicRateLimiter
		- 나노초 단위의 사이클 분할
		- 매 사이클 시작 시점에 가용한 허용수 설정
		- 가용한 허용 수가 충분하지 않으면 현재 허용 수를 줄이고 여유가 생길 때까지 대기

## 벌크헤드 vs 속도 제한
- 동시 횟수 차단 -> 벌크헤드
- 특정 기간의 총 호출 수 제한 -> 속도 제한

## ThreadLocal
- Resillience4j Annotation을 사용하는 메서드에서 부모 스레드의 값 사용 가능
