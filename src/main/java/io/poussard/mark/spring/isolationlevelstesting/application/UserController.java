package io.poussard.mark.spring.isolationlevelstesting.application;

import io.poussard.mark.spring.isolationlevelstesting.domain.*;
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
    private UserServiceFactory userServiceFactory;
    @Autowired
    private ConcurrencyTestService concurrencyTestService;

    @PostMapping
    public ResponseEntity<GenericUser> createUser(@RequestBody CreateUserRequest request, @RequestParam Isolation isolation, @RequestParam IndexingMode index) {
        UserService<?> service = userServiceFactory.getUserService(index);
        GenericUser user = service.create(request, isolation);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<? extends GenericUser>> getUsers(@RequestParam IndexingMode index) {
        UserService<?> service = userServiceFactory.getUserService(index);
        List<? extends GenericUser> users = service.getAll();
        return new ResponseEntity<>(users, HttpStatus.CREATED);
    }

    // Try this with different isolation levels
    @PostMapping("test/concurrent-create")
    public ResponseEntity<ConcurrencyTestResult<GenericUser>> testConcurrentCreate(@RequestBody CreateUserRequest request, @RequestParam Isolation isolation, @RequestParam IndexingMode index) {
        UserService<?> service = userServiceFactory.getUserService(index);
        ConcurrencyTestResult<GenericUser> result = concurrencyTestService.runConcurrently(_ -> service.create(request, isolation), 20);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // Try this with different isolation levels
    @PostMapping("test/neighbour-create")
    public ResponseEntity<ConcurrencyTestResult<GenericUser>> testNeighbourCreate(@RequestBody CreateUserRequest request, @RequestParam Isolation isolation, @RequestParam IndexingMode index) {
        UserService<?> service = userServiceFactory.getUserService(index);
        ConcurrencyTestResult<GenericUser> result = concurrencyTestService.runConcurrently(i -> {
            CreateUserRequest neighbourRequest = new CreateUserRequest(
                    request.getName() + i,
                    request.getEmail(),
                    request.getPassword()
            );
            return service.create(neighbourRequest, isolation);
        }, 20);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
