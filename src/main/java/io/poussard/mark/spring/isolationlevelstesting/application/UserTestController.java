package io.poussard.mark.spring.isolationlevelstesting.application;

import io.poussard.mark.spring.isolationlevelstesting.domain.UserTestService;
import io.poussard.mark.spring.isolationlevelstesting.infrastructure.UserTestResult;
import io.poussard.mark.spring.isolationlevelstesting.infrastructure.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/test/")
public class UserTestController {

    @Autowired
    private UserTestService userTestService;

    // Try this with different isolation levels on UserService.createUser
    @PostMapping("concurrent-writes")
    public ResponseEntity<UserTestResult> triggerConcurrentWrites(@RequestBody CreateUserRequest request, @RequestParam Isolation isolation) {
        UserTestResult result = userTestService.triggerConcurrentWrites(request, isolation);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
