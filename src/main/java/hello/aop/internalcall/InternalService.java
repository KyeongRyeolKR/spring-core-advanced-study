package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 내부에서 실행했던 메서드를 외부 클래스로 분리함
 */
@Slf4j
@Component
public class InternalService {

    public void internal() {
        log.info("call internal");
    }
}
