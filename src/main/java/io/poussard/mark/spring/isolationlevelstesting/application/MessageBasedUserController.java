package io.poussard.mark.spring.isolationlevelstesting.application;

import io.poussard.mark.spring.isolationlevelstesting.domain.messaging.MessageBasedUserService;
import io.poussard.mark.spring.isolationlevelstesting.domain.transaction.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/msg-based")
public class MessageBasedUserController {

    @Autowired
    private MessageBasedUserService messageBasedUserService;
    @Autowired
    private ConcurrencyTestService concurrencyTestService;

    @PostMapping
    public ResponseEntity<GenericUser> createUser(@RequestBody CreateUserRequest request, @RequestHeader(value = "Idempotency-Key") String idempotencyKey) {
        GenericUser user = messageBasedUserService.create(request, idempotencyKey);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<? extends GenericUser>> getUsers(@RequestParam IndexingMode index) {
        List<? extends GenericUser> users = messageBasedUserService.getAll();
        return new ResponseEntity<>(users, HttpStatus.CREATED);
    }

    // Try this with different isolation levels
    @PostMapping("test/concurrent-create")
    public ResponseEntity<ConcurrencyTestResult<GenericUser>> testConcurrentCreate(@RequestBody CreateUserRequest request) {
        ConcurrencyTestResult<GenericUser> result = concurrencyTestService.runConcurrently(_ -> messageBasedUserService.create(request, UUID.randomUUID().toString()), 20);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // Try this with different isolation levels
    @PostMapping("test/neighbour-create")
    public ResponseEntity<ConcurrencyTestResult<GenericUser>> testNeighbourCreate(@RequestBody CreateUserRequest request) {
        ConcurrencyTestResult<GenericUser> result = concurrencyTestService.runConcurrently(i -> {
            CreateUserRequest neighbourRequest = new CreateUserRequest(
                    request.getName() + i,
                    request.getEmail(),
                    request.getPassword()
            );
            return messageBasedUserService.create(neighbourRequest, UUID.randomUUID().toString());
        }, 20);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
