package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * 모든 어드바이스는 'JoinPoint' 를 첫번째 파라미터에 사용할 수 있다. (생략 가능)
 * 하지만 @Around는 target을 직접 실행해야하기 때문에 'ProceedingJoinPoint' 를 사용해야 한다. (생략 불가)
 */
@Slf4j
@Aspect
public class AspectV6Advice {

//    /**
//     * 메서드 주변(전/후)에 실행된다. (가장 강력한 어드바이스)
//     * joinPoint를 실행할지 말지 선택할 수 있다. (여러번 실행도 가능)
//     */
//    @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
//    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
//        try {
//            // @Before
//            log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
//            Object result = joinPoint.proceed();
//
//            // @AfterReturning
//            log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
//            return result;
//        } catch (Exception e) {
//            // @AfterThrowing
//            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
//            throw e;
//        } finally {
//            // @After
//            log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
//        }
//    }

    /**
     * 실제 target 로직이 실행되기 직전까지의 advice를 담당함
     * joinPoint(실제 target 로직)를 실행시키는 로직은 작성하지 않아도 알아서 실행해준다.
     */
    @Before("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[before] {}", joinPoint.getSignature());
    }

    /**
     * 실제 target 로직이 성공적으로 실행되고나서의 advice를 담당함
     * return 되는 값을 조작은 할 수 있지만, 완전히 다른것으로 바꿀 순 없다.
     * ps. 반환 타입이 잘맞아야한다! ex) 반환 타입이 String인데 Integer로 지정하면 실행조차 되지 않음
     */
    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result) {
        log.info("[return] {} return = {}", joinPoint.getSignature(), result);
    }

    /**
     * 실제 target 로직이 실행되고 예외가 발생하고나서의 advice를 담당함
     * 발생한 예외를 자동으로 throw 해줌
     */
    @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex) {
        log.info("[ex] {} message={}", joinPoint.getSignature(), ex.getMessage());
    }

    /**
     * finally에 해당하는 advice
     * 메서드 실행이 완전히 끝나면 실행된다.
     * 정상이든 예외든 모두 처리한다.
     * 일반적으로 리소스를 해제하는데 사용한다.
     */
    @After(value = "hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[after] {}", joinPoint.getSignature());
    }
}
