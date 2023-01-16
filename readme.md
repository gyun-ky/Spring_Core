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
---
### 템플릿 메서드 패턴
#### 핵심 기능과 부가 기능의 분리
* 로그 기능은 부가 기능, 핵심 기능은 주문 로직
* 부가 기능을 구현하기 위한 코드 구조는 같고 핵심 코드만 다름
* 부가 기능으로 인해 코드가 복잡해지는 문제점을 해결하기 위해 템플릿 메서드 패턴을 도입
* 변하는 부분과 변하지 않는 부분을 분리하는 마인드 필요!

#### GOF의 디자인 패턴 정의
* 작업에서 알고리즘의 골격을 정의, 일부 단계를 하위 클래스로 연계
* 부모 클래스에 알고리즘 골격인 템플릿을 정의 -> 변경되는 로직은 자식 클래스에 정의
  * 특정 부분만 재정의 가능 = 오버라이딩
  * 상속 + 오버라이딩

#### 구조
* AbstractTemplate
  * execute() - 변하지 않는 부분 ex.log 기록 부분
  * call() - 변하는 부분을 execute 안에서 call 함수를 호출하여서 진행
* SubClassLogic1 ~ 2
  * AbstractTemplate을 구체화
  * call 메서드를 오버라이드 하여서 비즈니스 로직을 구현

```java
        AbstractTemplate<String> template = new AbstractTemplate<String>(trace) {
            @Override
            protected String call() {
                orderServiceV4.orderItem(itemId);
                return "ok";
            }
        };
        return template.execute("OrderController.request()");
```
* call 추상 메서드를 오버라이드하려서 서비스 로직을 넣는다.
* execute 메서드에 LogTrace 인스턴트를 파라미터로 전달하여서 서비스 로직을 포함하고 있는 로그기록 부분 코드를 실행


#### 단점
* 상속에서 오는 단점으로 가져감
* 특정 부모 클래스를 의존!! 하고 있다 -> 부모 클래스를 사용하든, 안하든 강하게 의존하고 있다!!
* 부모 클래스를 수정하게 되면 모든 자식 클래스가 영향을 받게 됨

---
### 전략 패턴
* 상속이 아닌 위임으로 표현
* context가 strategy에 위임
* Spring 의존관계 주입에서 사용하고 있는 것이 전략패턴
* Context는 Strategy 인턴페이스에만 의존하고 있다

#### 방식
* 선 조립 후 실행 방식 - 조립후에는 context만 execute하면 된다
* Context의 execute의 인자로 strategy를 넘겨서 실행

#### 구조
* Context
* Strategy


#### 단점
* 조립 후에는 전략을 변경하기가 번거롭다 -> Context 파라미터에 Strategy를 넘기는 방식을 사용해서 유연성을 높일 수 있음
* Context를 싱글톤으로 사용할 경우 변경 중에 동시성 이슈가 있을 수 있다 -> 차라리 Context를 새로 생성해서 사용

익명 내부 클래스로 strategy 생성하고 context로 의존성 주입하여 실행
```java
  Strategy logic1 = new Strategy() {
    @Override
    public void call() {
        log.info("비즈니스 로직1");
        }
    };

    ContextV1 context1 = new ContextV1(logic1);
    log.info("strategy Logic1 = {}", logic1.getClass());
    context1.execute();
```
* context1이 execute를 실행할 떄, callback 함수인 strategy가 실행된다.

lambda로 코드 변환
* 인터페이스에서 메서드가 1개만 있으면 사용 가능
```java
  ContextV1 context1 = new ContextV1(() -> log.info("비즈니스 로직1"));
```

### 템플릿 콜백 패턴
* `콜백(callback)` 
  * 다른 코드의 인수로서 넘겨주는 실행가능한 코드
  * 코드가 호출(call) 되는 데 넘겨준 곳의 뒤(back)에서 실행된다는 의미
  * Java에서는 콜백함수를 만들기 위해서는 객체가 필요
    * java 8 이전 : 하나의 메서드 가진 인터페이스 + 익명 내부 클래스
    * java 8 이후 : 람다 사용 가
* GOF에서 나온 패턴은 아님 -> 스프링에서 많이 사용해서 Template(Context)와 CallBack(Strategy) 부분이 강조된 패턴
  * ex) JdbcTemplate, RestTemplate ....


#### 구조
1. 클라이언트는 Template을 실행하면서 CallBack C()를 전달
2. Template는 execute()를 시작하면서 callback.call()
3. C()가 실행됨
4. Template는 execute()를 종료함.




