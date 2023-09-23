package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 매칭 방법 : execution(접근제어자? 반환타입 선언타입?메서드이름(파라미터) 예외?)
 * ps. ?는 생략 가능
 */
@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    /**
     * 'execution' 으로 시작하는 포인트컷 표현식은 아래의 메서드 정보를 매칭해서 포인트컷 대상을 찾아낸다!
     */
    @Test
    void printMethod() {
        // helloMethod = public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        log.info("helloMethod = {}", helloMethod);
    }

    /**
     * 가장 정확히 매칭하는 포인트컷
     */
    @Test
    void exactMatch() {
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 가장 많이 생략한 포인트컷
     */
    @Test
    void allMatch() {
        pointcut.setExpression("execution(* *(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 메서드 이름으로 매칭하는 포인트컷
     */
    @Test
    void nameMatch() {
        pointcut.setExpression("execution(* hello(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 메서드 이름의 패턴으로 매칭하는 포인트컷1
     */
    @Test
    void nameMatchStar1() {
        pointcut.setExpression("execution(* hel*(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 메서드 이름의 패턴으로 매칭하는 포인트컷2
     */
    @Test
    void nameMatchStar2() {
        pointcut.setExpression("execution(* *el*(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 메서드 이름 매칭에 실패하는 포인트컷
     */
    @Test
    void nameMatchFalse() {
        pointcut.setExpression("execution(* nono(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    /**
     * 정확한 패키지 이름으로 매칭하는 포인트컷1
     */
    @Test
    void packageExactMatch1() {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 정확한 패키지 이름으로 매칭하는 포인트컷2
     */
    @Test
    void packageExactMatch2() {
        pointcut.setExpression("execution(* hello.aop.member.*.*(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 패키지 매칭이 실패하는 포인트컷
     */
    @Test
    void packageExactFalse() {
        pointcut.setExpression("execution(* hello.aop.*.*(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    /**
     * 해당 패키지와 그 하위 패키지를 매칭하는 포인트컷
     * '.' 은 정확히 해당 패키지만 포함하지만,
     * '..' 은 해당 패키지와 그 하위 패키지도 포함한다.
     * -> hello -> aop -> member 패키지와 그 하위 패키지의 모든 클래스 및 모든 메서드 및 모든 파라미터
     */
    @Test
    void packageMatchSubPackage1() {
        pointcut.setExpression("execution(* hello.aop.member..*.*(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 해당 패키지와 그 하위 패키지를 매칭하는 포인트컷
     * '.' 은 정확히 해당 패키지만 포함하지만,
     * '..' 은 해당 패키지와 그 하위 패키지도 포함한다.
     * -> hello -> aop 패키지와 하위 패키지의 모든 클래스 및 모든 메서드 및 모든 파라미터
     */
    @Test
    void packageMatchSubPackage2() {
        pointcut.setExpression("execution(* hello.aop..*.*(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 클래스 타입으로 정확히 매칭하는 포인트컷
     */
    @Test
    void typeExactMatch() {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 부모 타입으로 매칭하는 포인트컷
     */
    @Test
    void typeMatchSuperType() {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 타입으로 매칭하는 포인트컷
     */
    @Test
    void typeMatchInternal() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");

        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);

        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 부모 타입으로 매칭하는 포인트컷
     * 하지만 부모 타입에 정의되어 있는 메서드까지만 적용이 된다!
     * 즉, 자식 타입에만 있는 메서드들은 적용되지 않는다.
     */
    @Test
    void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");

        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);

        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
    }

    /**
     * String 타입의 파라미터를 매칭하는 포인트컷
     */
    @Test
    void argsMatch() {
        pointcut.setExpression("execution(* *(String))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 파라미터가 아예 없는 메서드를 매칭하는 포인트컷
     */
    @Test
    void argsMatchNoArgs() {
        pointcut.setExpression("execution(* *())");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    /**
     * 정확히 하나의 파라미터만 매칭하는 포인트컷
     * - 어떤 타입이든 파라미터가 하나만 존재하는 모든 메서드 매칭
     */
    @Test
    void argsMatchStar() {
        pointcut.setExpression("execution(* *(*))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 모든 파라미터 개수, 모든 타입을 매칭하는 포인트컷
     */
    @Test
    void argsMatchAll() {
        pointcut.setExpression("execution(* *(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * String 타입으로 시작하고 모든 파라미터 개수, 모든 타입을 매칭하는 포인트컷
     * - 참고로 '..' 은 0개도 포함이다.
     */
    @Test
    void argsMatchComplex() {
        pointcut.setExpression("execution(* *(String, ..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
