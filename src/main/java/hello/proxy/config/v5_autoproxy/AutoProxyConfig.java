package hello.proxy.config.v5_autoproxy;

import hello.advanced.trace.logtrace.LogTrace;
import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 스프링이 등록한 자동 프록시 생성기(AnnotationAwareAspectJAutoProxyCreator)는
 * 빈으로 등록된 advisor를 모조리 찾고,advisor가 존재한다면
 * 앞으로 등록할 빈들을 advisor의 pointcut을 기준으로 프록시를 생성한다.
 *
 * 즉, 단순히 advisor만 등록하면
 * advisor 내부의 pointcut을 보고 판단해서 애초에 프록시를 만들지? 안만들지?
 * 프록시가 만들어졌어도 advisor 내부의 pointcut을 보고 판단해서 메서드 호출 시 advice를 적용할지? 안할지?
 * 이런 것들이 다 자동화가 된다!
 *
 * 그니까 결국 포인트컷은 2가지 용도로 사용된다.
 * 1) 프록시 생성 여부 판단 - 애초에 프록시가 필요한지? 안한지?
 * 2) 어드바이스 적용 여부 판단 - 프록시지만 부가 기능 추가가 필요한지? 안한지?
 */
@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AutoProxyConfig {

    @Bean
    public Advisor advisor1(LogTrace logTrace) {
        // pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
