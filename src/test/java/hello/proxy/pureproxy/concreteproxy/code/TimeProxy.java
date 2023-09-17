package hello.proxy.pureproxy.concreteproxy.code;

import lombok.extern.slf4j.Slf4j;

/**
 * 구체 클래스 상속으로 프록시 적용
 * - 시간을 측정하는 부가 기능 추가
 */
@Slf4j
public class TimeProxy extends ConcreteLogic {

    private ConcreteLogic concreteLogic;

    public TimeProxy(ConcreteLogic concreteLogic) {
        this.concreteLogic = concreteLogic;
    }

    @Override
    public String operation() {
        log.info("TimeDecorator 실행");
        long startTime = System.currentTimeMillis();

        String result = concreteLogic.operation();

        long endTime = System.currentTimeMillis();

        long resultTime = endTime - startTime;
        log.info("TimeDecorator 종료 resultTime = {}ms", resultTime);

        return result;
    }
}
