package hello.proxy.pureproxy.proxy;

import hello.proxy.pureproxy.proxy.code.CacheProxy;
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

    /**
     * 프록시 패턴으로 캐싱을 사용하여 처음 호출 때는 실제 객체를 호출하지만,
     * 다음 호출부터는 실제 객체 호출이 아닌 프록시에서 캐시된 데이터를 즉시 반환한다.
     * 즉, 총 1초정도 걸림
     */
    @Test
    void cacheProxyTest() {
        RealSubject realSubject = new RealSubject();
        CacheProxy cacheProxy = new CacheProxy(realSubject);
        ProxyPatternClient client = new ProxyPatternClient(cacheProxy);

        client.execute();
        client.execute();
        client.execute();
    }
}
