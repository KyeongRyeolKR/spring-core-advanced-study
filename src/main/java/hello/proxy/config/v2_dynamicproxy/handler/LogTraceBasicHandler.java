package hello.proxy.config.v2_dynamicproxy.handler;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogTraceBasicHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace logTrace;

    public LogTraceBasicHandler(Object target, LogTrace logTrace) {
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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
