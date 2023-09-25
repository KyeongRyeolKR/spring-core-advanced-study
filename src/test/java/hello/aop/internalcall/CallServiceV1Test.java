package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(CallLogAspect.class)
@SpringBootTest
class CallServiceV1Test {

    @Autowired
    CallServiceV1 callServiceV1;

    /**
     * this 예약어 대신 자기 자신을 주입 받아서(프록시 주입됨) 내부 메서드를 호출해서 해결할 수 있다!
     * 결국 자기 자신 인스턴스의 내부 메서드를 호출하는 것이 아닌 주입 받은 프록시의 메서드를 호출하는 것이다!
     * ex) (this.)internal() -> callServiceV1.internal()
     */
    @Test
    void external() {
        callServiceV1.external();
    }

}