package hello.proxy.pureproxy.proxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheProxy implements Subject {

    private Subject target; // 실제 객체
    private String cacheValue; // 캐시 데이터

    public CacheProxy(Subject target) {
        this.target = target;
    }

    @Override
    public String operation() {
        log.info("프록시 호출");

        // 첫 호출 시, 캐시된 데이터가 없으므로 실제 객체를 호출한다.
        if(cacheValue == null) {
            cacheValue = target.operation();
        }

        // 캐시 데이터가 있다면 실제 객체를 호출하지 않고 바로 캐시된 데이터를 반환한다.
        return cacheValue;
    }
}
