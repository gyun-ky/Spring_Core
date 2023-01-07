# Spring Core 

### 로그 추적기 구현
#### 싱글톤 객체로 관리
* 로그 기록하는 클래스를 Spring Component로 등록하여서 Singleton 패턴으로 관리
    
    -> 문제점 : 동시성 문제 
  * 여러개의 thread가 동시에 api에 접근했을 시, 인스턴스의 변수를 변경하게 된다면 엉키게 된다.
  * n : 1 = thread : 인스턴스변수 by 싱글톤

#### ThreadLocal을 사용하기
* 해당 쓰레드만 접근 가능한 저장소
* TraceId 변수 필드를 ThreadLocal로 선언하여서 각 쓰레드 마다 고유의 저장소를 만들어준다
* 대신 Thread는 pool에서 랜덤으로 선택되어서 차출?되기 때문에 threadLocal 저장소는 request에서 다 사용후 반드시 remove 해주어야 함!
### 템플릿 메서드 패턴
#### 핵심 기능과 부가 기능의 분리
* 로그 기능은 부가 기능, 핵심 기능은 주문 로직
* 부가 기능을 구현하기 위한 코드 구조는 같고 핵심 코드만 다름
* 부가 기능으로 인해 코드가 복잡해지는 문제점을 해결하기 위해 템플릿 메서드 패턴을 도입
* 변하는 부분과 변하지 않는 부분을 분리하는 마인드 필요!

#### 구조
* AbstractTemplate
  * execute() - 변하지 않는 부분 ex.log 기록 부분
  * call() - 변하는 부분을 execute 안에서 call 함수를 호출하여서 진행
* SubClassLogic1 ~ 2
  * AbstractTemplate을 구체화
  * call 메서드를 오버라이드 하여서 비즈니스 로직을 구현