package io.poussard.mark.spring.isolationlevelstesting.application;

import io.poussard.mark.spring.isolationlevelstesting.domain.User;
import io.poussard.mark.spring.isolationlevelstesting.domain.UserService;
import io.poussard.mark.spring.isolationlevelstesting.infrastructure.CreateUserRequest;
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

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request, @RequestParam Isolation isolation) {
        User createdUser = userService.createUser(request, isolation);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.CREATED);
    }
}
