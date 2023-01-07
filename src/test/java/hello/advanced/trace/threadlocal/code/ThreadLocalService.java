package hello.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalService {

    private ThreadLocal<String> nameStore = new ThreadLocal<>(); // 쓰레드가 ThreadLocal을 모두 사용하게 되면 remove로 반드시 제거해주어야 한다.

    public String logic(String name) {
        log.info("저장 name={} --> nameStore={}", name, nameStore.get());
        nameStore.set(name);
        sleep(1000);
        log.info("조회 nameStore={}", nameStore.get());
        return nameStore.get();
    }

    private void sleep(int milllis) {
        try {
            Thread.sleep(milllis);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
