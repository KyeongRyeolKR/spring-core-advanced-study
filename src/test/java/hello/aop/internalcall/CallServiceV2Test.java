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
class CallServiceV2Test {

    @Autowired
    CallServiceV2 callServiceV2;

    /**
     * 객체를 스프링 컨테이너에서 조회하는 거슬 스프링 빈 생성 시점이 아니라
     * 실제 객체를 사용하는 시점으로 지연하는 방법!
     * - 여기서는 자기 자신을 주입 받는 것이 아니기 때문에 순환 참조가 발생하지 않는다.
     */
    @Test
    void external() {
        callServiceV2.external();
    }

}