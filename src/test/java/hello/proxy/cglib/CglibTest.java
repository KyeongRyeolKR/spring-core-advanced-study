package hello.proxy.cglib;

import hello.proxy.cglib.code.TimeMethodInterceptor;
import hello.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

/**
 * CGLIB 제약
 * 클래스 기반 프록시는 상속을 사용하기 때문에 몇가지 제약이 있다.
 * - 자식 클래스를 동적으로 생성하기 때문에 기본 생성자가 필요하다.
 * - 클래스에 final 키워드가 붙으면 상속이 불가능하다.
 * - 메서드에 final 키워드가 붙으면 해당 메서드를 오버라이딩 할 수 없다.
 */
@Slf4j
public class CglibTest {

    @Test
    void cglib() {
        ConcreteService target = new ConcreteService();

        // 프록시 생성 로직
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ConcreteService.class); // 프록시의 부모(구체) 클래스 지정
        enhancer.setCallback(new TimeMethodInterceptor(target)); // 프록시에 적용할 실행 로직 지정
        ConcreteService proxy = (ConcreteService) enhancer.create(); // 프록시 생성

        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());

        proxy.call();
    }
}
