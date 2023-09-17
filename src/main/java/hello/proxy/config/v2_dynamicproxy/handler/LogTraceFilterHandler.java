package hello.proxy.config.v2_dynamicproxy.handler;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogTraceFilterHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace logTrace;
    private final String[] patterns;

    public LogTraceFilterHandler(Object target, LogTrace logTrace, String[] patterns) {
        this.target = target;
        this.logTrace = logTrace;
        this.patterns = patterns;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 메서드 이름 필터
        String methodName = method.getName();

        // 특정 패턴에 메서드 이름이 매칭이 되지 않는다면,
        // 부가 기능을 적용 하지 않고 실제 객체 메서드 로직 그대로 실행
        if(!PatternMatchUtils.simpleMatch(patterns, methodName)) {
            return method.invoke(target, args);
        }

        TraceStatus status = null;
        try {
            // 클래스명.메서드명() 형식으로 메시지 생성
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            
            status = logTrace.begin(message);

            // target(실제 객체) 호출
            Object result = method.invoke(target, args);

            logTrace.end(status);

            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
