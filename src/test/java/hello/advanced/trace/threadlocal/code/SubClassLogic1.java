package hello.advanced.trace.threadlocal.code;

import hello.advanced.trace.template.code.AbstractTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubClassLogic1 extends AbstractTemplate {

    @Override
    protected void call() {
        log.info("비즈니스 로직1 실행");
    }
}
