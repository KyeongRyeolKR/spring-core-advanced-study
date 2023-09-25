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
class CallServiceV0Test {

    @Autowired
    CallServiceV0 callServiceV0;

    /**
     * external() 메서드에서 internal() 이라는 내부 메서드를 사용하면,
     * internal() 메서드는 AOP가 적용되지 않는다.
     * 왜냐하면 내부 메서드를 사용할땐(별도의 참조가 없을때) this 예약어가 생략되어 있다.
     * 결과적으로 자기 자신 인스턴스의 this.internal()이 되기 때문에 프록시를 거치고 실행되는 것이 아닌,
     * 실제 대상 객체의 로직을 그대로 실행하게 된다. 따라서 어드바이스를 적용할 수 없다!!
     *
     * 그렇다면 aspectj 프레임워크를 사용하지 않고 스프링 AOP만을 사용해서는
     * 내부 호출 메서드에는 절대 어드바이스를 적용할 수 없을까?
     */
    @Test
    void external() {
        callServiceV0.external();
    }

    @Test
    void internal() {
        callServiceV0.internal();
    }
}