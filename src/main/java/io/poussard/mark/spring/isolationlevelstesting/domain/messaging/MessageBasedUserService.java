package io.poussard.mark.spring.isolationlevelstesting.domain.messaging;

import io.poussard.mark.spring.isolationlevelstesting.domain.transaction.*;
import io.poussard.mark.spring.isolationlevelstesting.domain.transaction.noidxuser.NoIdxUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class MessageBasedUserService {
    @Autowired
    private KafkaTemplate<String, CreateUserMessage> createUserKafkaTemplate;
    @Autowired
    private KafkaTemplate<String, CreateUserOutputMessage> createUserOutputKafkaTemplate;
    @Autowired
    private SyncMessagingService syncMessagingService;
    @Autowired
    private UserServiceFactory userServiceFactory;

    private UserService<?> getUserService(){
        return userServiceFactory.getUserService(IndexingMode.NO_INDEX);
    }

    public List<? extends GenericUser> getAll() {
        return getUserService().getAll();
    }
    
    public GenericUser create(CreateUserRequest request, String idempotencyKey){
        try {
            // Important to get the future before sending the create user message due to the internal hashmap cleanup mechanism
            CompletableFuture<GenericUser> future = syncMessagingService.get(idempotencyKey);
            createUserKafkaTemplate.send("create-user", new CreateUserMessage(
                    idempotencyKey,
                    request
            ));
            return future.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    // The user creation processing has been split between a create-user topic processor followed by a create-user-output topic processor
    // This is to illustrate the idea that the user creation processing could be running on another node,
    // allowing partitioning of the "create-user" topic for better throughput.
    // create-user -> partitioned by user name
    // create-user-output -> partitioned in the same manner as the initial request processor to allow the same node to respond back to the user
    @KafkaListener(topics = "create-user", groupId = "my-group")
    public void create(CreateUserMessage message){
        try{
            GenericUser user = getUserService().create(message.getRequest(), Isolation.DEFAULT);
            createUserOutputKafkaTemplate.send("create-user-output", new CreateUserOutputMessage(message.getIdempotencyKey(), null, (NoIdxUser) user));
        } catch (Exception e){
            createUserOutputKafkaTemplate.send("create-user-output", new CreateUserOutputMessage(message.getIdempotencyKey(), e.getMessage(), null));
        }
    }

    @KafkaListener(topics = "create-user-output", groupId = "my-group")
    public void handleCreateOutput(CreateUserOutputMessage message){
        if(message.getUser() != null){
            syncMessagingService.set(message.getIdempotencyKey(), message.getUser());
        } else {
            syncMessagingService.set(message.getIdempotencyKey(), new RuntimeException(message.getError()));
        }
    }
}
