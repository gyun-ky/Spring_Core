package hello.advanced.trace.threadlocal;

import hello.advanced.trace.threadlocal.code.FieldService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class FieldServiceTest {

    private FieldService fieldService = new FieldService();

    @Test
    void field() {
        log.info("main start");

//        Runnable userT = new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        };

        Runnable userA = () -> {
            fieldService.logic("userA");
        };

        Runnable userB = () -> {
            fieldService.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");

        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");


        threadA.start();
//        sleep(2000); // 동시성 문제가 발생하지 않는 경우
        sleep(100); // 동시성 문제가 발생하는 경우
        threadB.start();
        sleep(3000); // main 쓰레드 종료 대기 - 두번째 로그가 안나올 수 있음
        log.info("main exit");
    }

    private void sleep(int milllis) {
        try {
            Thread.sleep(milllis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
