package hello.proxy.pureproxy.decorator;

import hello.proxy.pureproxy.decorator.code.Component;
import hello.proxy.pureproxy.decorator.code.DecoratorPatternClient;
import hello.proxy.pureproxy.decorator.code.RealComponent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 데코레이터 패턴의 주요 기능은 '부가 가능 추가' 이다.
 * - 원래 서버가 제공하는 기능에 더해서 부가 기능을 수행한다.
 * ex) 요청 값이나 응답 값을 중간에 변형한다.
 * ex) 실행 시간을 측정해서 추가 로그를 남긴다.
 */
@Slf4j
public class DecoratorPatternTest {

    /**
     * 데코레이터 패턴 적용 전, 순수한 서버 기능만 실행
     */
    @Test
    void noDecorator() {
        Component realComponent = new RealComponent();
        DecoratorPatternClient client = new DecoratorPatternClient(realComponent);

        client.execute();
    }
}
