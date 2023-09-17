package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * 리플렉션을 사용하면 클래스와 메서드의 메타정보를 사용해서 애플리케이션을 동적으로 만들 수 있다.
 * 하지만 리플레션 기술은 런타임에 동작하기 때문에 '컴파일 오류'를 잡을 수 없다!
 * p.s. 프레임워크 개발이나 매우 일반적인 공통 처리가 필요할 때 부분적으로 조심히 사용하자
 */
@Slf4j
public class ReflectionTest {

    @Test
    void reflection0() {
        Hello target = new Hello();

        // 공통 로직1 시작
        log.info("start");
        String result1 = target.callA(); // 호출하는 메서드만 다름
        log.info("result = {}", result1);
        // 공통 로직1 종료

        // 공통 로직2 시작
        log.info("start");
        String result2 = target.callB(); // 호출하는 메서드만 다름
        log.info("result = {}", result2);
        // 공통 로직2 종료
    }

    @Test
    void reflection1() throws Exception {
        // 클래스 메타 정보 획득
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();

        // callA() 메서드 정보 획득
        Method methodCallA = classHello.getMethod("callA");
        // 메서드 호출
        Object result1 = methodCallA.invoke(target);
        log.info("result1 = {}", result1);

        // callB() 메서드 정보 획득
        Method methodCallB = classHello.getMethod("callB");
        // 메서드 호출
        Object result2 = methodCallB.invoke(target);
        log.info("result2 = {}", result2);
    }

    @Test
    void reflection2() throws Exception {
        // 클래스 메타 정보 획득
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();

        // callA() 메서드 정보 획득
        Method methodCallA = classHello.getMethod("callA");
        // 메서드 호출
        dynamicCall(methodCallA, target);

        // callB() 메서드 정보 획득
        Method methodCallB = classHello.getMethod("callB");
        // 메서드 호출
        dynamicCall(methodCallB, target);
    }

    /**
     * 동적 메서드 호출(리플렉션) - 공통화
     */
    private void dynamicCall(Method method, Object target) throws Exception {
        log.info("start");
        String result = (String) method.invoke(target);
        log.info("result = {}", result);
    }

    @Slf4j
    static class Hello {
        public String callA() {
            log.info("callA");
            return "A";
        }
        public String callB() {
            log.info("callB");
            return "B";
        }
    }
}
