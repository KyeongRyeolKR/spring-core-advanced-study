package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * 어드바이스에 매개변수를 전달하는 방법
 */
@Slf4j
@Import(ParameterTest.ParameterAspect.class)
@SpringBootTest
public class ParameterTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService Proxy = {}", memberService.getClass());

        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ParameterAspect {

        /**
         * 재사용할 포인트컷 표현식
         */
        @Pointcut("execution(* hello.aop.member..*.*(..))")
        private void allMember() {}

        /**
         * 배열을 직접 꺼내서 사용하는 방법
         */
        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object args1 = joinPoint.getArgs()[0];
            log.info("[logArgs1]{}, args = {}", joinPoint.getSignature(), args1);
            return joinPoint.proceed();
        }

        /**
         * args(파라미터) 를 사용해서 꺼내는 방법
         */
        @Around("allMember() && args(arg, ..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
            log.info("[logArgs2]{}, args = {}", joinPoint.getSignature(), arg);
            return joinPoint.proceed();
        }

        /**
         * '@Before' 와 args(파라미터) 를 사용해서 꺼내는 방법
         * - 제일 깔끔함, 타입을 지정해야하고 변수명을 똑같이 맞춰줘야함
         * - '@Before' 는 실제 로직 직전의 어드바이스이기 때문에 조인포인트와 반환 값이 없다!
         */
        @Before("allMember() && args(arg,..)")
        public void logArgs3(String arg) {
            log.info("[logArgs3] args = {}", arg);
        }

        /**
         * this(인스턴스) 를 사용해서 인스턴스를 꺼내는 방법
         * this 는 target 과 다르게 스프링 빈에 등록된 객체(프록시)를 가져온다.
         * - 조인포인트를 파라미터로 받지 않아도 됨. 여기서는 getSignature() 를 호출하기 위해 넣었다.
         */
        @Before("allMember() && this(obj)")
        public void thisArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[this]{}, obj = {}", joinPoint.getSignature(), obj.getClass());
        }

        /**
         * target(인스턴스) 를 사용해서 인스턴스를 꺼내는 방법
         * target 은 this 와 다르게 실제 구현 객체를 가져온다.
         * - 조인포인트를 파라미터로 받지 않아도 됨. 여기서는 getSignature() 를 호출하기 위해 넣었다.
         */
        @Before("allMember() && target(obj)")
        public void targetArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[target]{}, obj = {}", joinPoint.getSignature(), obj.getClass());
        }

        /**
         * @target 을 사용해서 애노테이션 정보를 꺼내는 방법
         */
        @Before("allMember() && @target(annotation)")
        public void atTarget(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@target]{}, obj = {}", joinPoint.getSignature(), annotation);
        }

        /**
         * @within 을 사용해서 타입의 애노테이션 정보를 꺼내는 방법
         */
        @Before("allMember() && @within(annotation)")
        public void atWithin(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@within]{}, obj = {}", joinPoint.getSignature(), annotation);
        }

        /**
         * @annotation 을 사용해서 메서드의 애노테이션 정보 및 지정한 값을 꺼내는 방법
         */
        @Before("allMember() && @annotation(annotation)")
        public void atAnnotation(JoinPoint joinPoint, MethodAop annotation) {
            log.info("[@annotation]{}, annotationValue = {}", joinPoint.getSignature(), annotation.value());
        }
    }
}
