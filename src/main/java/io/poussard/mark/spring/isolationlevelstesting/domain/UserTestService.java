package io.poussard.mark.spring.isolationlevelstesting.domain;

import io.poussard.mark.spring.isolationlevelstesting.infrastructure.UserTestResult;
import io.poussard.mark.spring.isolationlevelstesting.infrastructure.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class UserTestService {
    @Autowired
    private UserService userService;

    public UserTestResult triggerConcurrentWrites(CreateUserRequest request, Isolation isolation){
        ConcurrentLinkedQueue<User> success = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<String> failures = new ConcurrentLinkedQueue<>();
        CompletableFuture[] tasks = new CompletableFuture[20];
        for(int i=0; i<20; i++){
            tasks[i] = CompletableFuture.runAsync(() -> {
                try{
                    success.add(userService.createUser(request, isolation));
                } catch(Exception e){
                    failures.add(e.getMessage());
                }
            });
        }
        CompletableFuture.allOf(tasks).join();
        return new UserTestResult(success.stream().toList(), failures.stream().toList());
    }
}
