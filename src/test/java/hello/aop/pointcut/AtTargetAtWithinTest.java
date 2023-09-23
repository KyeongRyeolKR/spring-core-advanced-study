package hello.aop.pointcut;

import hello.aop.member.annotation.ClassAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * '@target' 은 인스턴스의 모든 메서드를 조인 포인트로 적용한다.
 * 즉, 부모 타입과 자식 타입 모두 적용한다.
 *
 * '@within' 은 해당 타입 내에 있는 메서드만 조인 포인트로 적용한다.
 * 즉, 정확히 해당하는 타입만 적용한다.
 *
 * !! 주의 !!
 * 'args', '@args', '@target' 같은 포인트컷 지시자가 있으면, 스프링은 모든 빈에 AOP를 적용하려고 시도한다.
 * 왜냐하면 실행 시점에 일어나는 포인트컷 적용 여부도 결국 프록시가 존재해야 판단할 수 있는데
 * 스프링 컨테이너가 프록시를 생성하는 시점이 스프링 컨테이너가 만들어지는 애플리케이션 로딩 시점에 적용할 수 있기 때문이다.
 * 만약 모든 스프링 빈에 AOP 프록시를 적용하려하면 스프링 내부에서 사용하는 빈 중에서는 final 로 지정된 빈들도 존재하기에
 * 프록시 생성 자체가 불가능해지기 때문에 오류가 발생할 수 있다.
 *
 * 따라서 이러한 표현식들은 꼭!! 프록시 적용 대상을 execution 으로 적용 대상을 줄여서 함께 사용해야한다.
 */
@Slf4j
@Import(AtTargetAtWithinTest.Config.class)
@SpringBootTest
public class AtTargetAtWithinTest {

    @Autowired
    Child child;

    @Test
    void success() {
        log.info("child Proxy = {}", child.getClass());
        child.childMethod();
        child.parentMethod();
    }

    static class Config {
        @Bean
        public Parent parent() {
            return new Parent();
        }

        @Bean
        public Child child() {
            return new Child();
        }

        @Bean
        public AtTargetAtWithinAspect atTargetAtWithinAspect() {
            return new AtTargetAtWithinAspect();
        }
    }

    static class Parent {
        public void parentMethod() {}
    }

    @ClassAop
    static class Child extends Parent {
        public void childMethod() {} // 자식에만 있는 메서드
    }

    @Slf4j
    @Aspect
    static class AtTargetAtWithinAspect {

        // @target : 인스턴스 기준으로 모든 메서드의 조인 포인트를 선정, 부모 타입의 메서드도 적용
        @Around("execution(* hello.aop..*(..)) && @target(hello.aop.member.annotation.ClassAop)")
        public Object atTarget(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@target] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        // @within : 선택된 클래스 내부에 있는 메서드만 조인 포인트로 선정, 부모 타입의 메서드는 적용되지 않음
        @Around("execution(* hello.aop..*(..)) && @within(hello.aop.member.annotation.ClassAop)")
        public Object atWithin(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@within] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }
}
