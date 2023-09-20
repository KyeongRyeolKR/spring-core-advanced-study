package hello.proxy.config.v6_aop.aspect;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Method;

@Slf4j
// Advisor를 편리하게 생성해주는 애노테이션
@Aspect
public class LogTraceAspect {

    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    /**
     * Advisor
     */
    @Around("execution(* hello.proxy.app..*(..))") // 포인트컷
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        // 어드바이스
        TraceStatus status = null;
        try {
            // 로그 메시지 생성
            String message = joinPoint.getSignature().toShortString();

            status = logTrace.begin(message);

            // target(실제 객체) 호출
            Object result = joinPoint.proceed();

            logTrace.end(status);

            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
