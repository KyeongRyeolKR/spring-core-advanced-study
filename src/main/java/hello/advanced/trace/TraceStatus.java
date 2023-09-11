package hello.advanced.trace;

/**
 * 로그의 상태 정보들을 담은 클래스
 */
public class TraceStatus {

    private TraceId traceId;
    private Long startTimeMs; // 로그 시작 시간
    private String message;

    public TraceStatus(TraceId traceId, Long startTimeMs, String message) {
        this.traceId = traceId;
        this.startTimeMs = startTimeMs;
        this.message = message;
    }

    public TraceId getTraceId() {
        return traceId;
    }

    public Long getStartTimeMs() {
        return startTimeMs;
    }

    public String getMessage() {
        return message;
    }
}
