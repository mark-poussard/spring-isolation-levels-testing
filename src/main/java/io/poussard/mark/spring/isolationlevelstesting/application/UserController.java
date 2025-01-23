package io.poussard.mark.spring.isolationlevelstesting.application;

import io.poussard.mark.spring.isolationlevelstesting.domain.User;
import io.poussard.mark.spring.isolationlevelstesting.domain.UserService;
import io.poussard.mark.spring.isolationlevelstesting.domain.ConcurrencyTestService;
import io.poussard.mark.spring.isolationlevelstesting.infrastructure.CreateUserRequest;
import io.poussard.mark.spring.isolationlevelstesting.infrastructure.ConcurrencyTestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ConcurrencyTestService concurrencyTestService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request, @RequestParam Isolation isolation) {
        User createdUser = userService.create(request, isolation);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.CREATED);
    }

    // Try this with different isolation levels
    @PostMapping("test/concurrent-create")
    public ResponseEntity<ConcurrencyTestResult<User>> testConcurrentCreate(@RequestBody CreateUserRequest request, @RequestParam Isolation isolation) {
        ConcurrencyTestResult<User> result = concurrencyTestService.runConcurrently(_ -> userService.create(request, isolation), 20);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // Try this with different isolation levels
    @PostMapping("test/neighbour-create")
    public ResponseEntity<ConcurrencyTestResult<User>> testNeighbourCreate(@RequestBody CreateUserRequest request, @RequestParam Isolation isolation) {
        ConcurrencyTestResult<User> result = concurrencyTestService.runConcurrently(index -> {
            CreateUserRequest neighbourRequest = new CreateUserRequest(
                    request.getName() + index,
                    request.getEmail(),
                    request.getPassword()
            );
            return userService.create(neighbourRequest, isolation);
        }, 50);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
