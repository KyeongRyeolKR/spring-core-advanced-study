package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.strategy.ContextV2;
import hello.advanced.trace.strategy.code.strategy.Strategy;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV2Test {

    /**
     * 전략 패턴 사용
     * - 미리 조립 하는 것이 아닌 실행 시점에 전략을 파라미터로 전달하는 방식
     */
    @Test
    void strategyV1() {
        ContextV2 context = new ContextV2();

        context.execute(new StrategyLogic1());

        context.execute(new StrategyLogic2());
    }

    /**
     * 익명 내부 클래스 사용1
     */
    @Test
    void strategyV2() {
        ContextV2 context = new ContextV2();

        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        });

        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        });
    }

    /**
     * 익명 내부 클래스 사용2 - 람다 적용
     */
    @Test
    void strategyV3() {
        ContextV2 context = new ContextV2();

        context.execute(() -> log.info("비즈니스 로직1 실행"));

        context.execute(() -> log.info("비즈니스 로직2 실행"));
    }
}
