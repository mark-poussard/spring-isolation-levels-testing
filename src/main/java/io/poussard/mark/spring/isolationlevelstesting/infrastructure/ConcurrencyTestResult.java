package io.poussard.mark.spring.isolationlevelstesting.infrastructure;

import java.util.List;

public class ConcurrencyTestResult<T> {
    private List<T> success;
    private List<String> failures;
    private long duration;

    public ConcurrencyTestResult(List<T> success, List<String> failures, long duration) {
        this.success = success;
        this.failures = failures;
        this.duration = duration;
    }

    public List<T> getSuccess() {
        return success;
    }

    public List<String> getFailures() {
        return failures;
    }

    public long getDuration() {
        return duration;
    }
}
