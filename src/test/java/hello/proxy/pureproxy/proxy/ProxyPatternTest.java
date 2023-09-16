package hello.proxy.pureproxy.proxy;

import hello.proxy.pureproxy.proxy.code.ProxyPatternClient;
import hello.proxy.pureproxy.proxy.code.RealSubject;
import org.junit.jupiter.api.Test;

/**
 * 프록시 패턴의 주요 기능은 '접근 제어' 이다.
 * - 권한에 따른 접근 차단
 * - 캐싱
 * - 지연 로딩
 */
public class ProxyPatternTest {

    /**
     * 프록시 패턴을 적용하지 않았을 때는 각 호출 당 1초씩 총 3초가 걸림
     */
    @Test
    void noProxyTest() {
        RealSubject realSubject = new RealSubject();
        ProxyPatternClient client = new ProxyPatternClient(realSubject);

        client.execute();
        client.execute();
        client.execute();
    }
}
