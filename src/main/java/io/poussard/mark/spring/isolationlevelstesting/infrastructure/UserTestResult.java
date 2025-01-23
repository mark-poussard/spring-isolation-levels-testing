package io.poussard.mark.spring.isolationlevelstesting.infrastructure;

import io.poussard.mark.spring.isolationlevelstesting.domain.User;

import java.util.List;

public class UserTestResult {
    private List<User> success;
    private List<String> failures;

    public UserTestResult(List<User> success, List<String> failures) {
        this.success = success;
        this.failures = failures;
    }

    public List<User> getSuccess() {
        return success;
    }

    public List<String> getFailures() {
        return failures;
    }
}
