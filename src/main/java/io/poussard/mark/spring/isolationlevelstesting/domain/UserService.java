package io.poussard.mark.spring.isolationlevelstesting.domain;

import io.poussard.mark.spring.isolationlevelstesting.infrastructure.CreateUserRequest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

public class UserService<T extends GenericUser> {
    private final PlatformTransactionManager transactionManager;
    private final UserConstructor<T> constructor;
    private final GenericUserRepository<T> repository;

    UserService(PlatformTransactionManager transactionManager, UserConstructor<T> constructor, GenericUserRepository<T> repository){
        this.transactionManager = transactionManager;
        this.constructor = constructor;
        this.repository = repository;
    }

    public GenericUser create(CreateUserRequest request, Isolation isolation) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setIsolationLevel(isolation.value());
        return transactionTemplate.execute(_ -> internalCreate(request));
    }

    private T internalCreate(CreateUserRequest request) {
        T user = constructor.apply(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );
        if(!repository.findByName(user.getName()).isEmpty()){
            throw new IllegalArgumentException(String.format("User %s already exists.", user.getName()));
        }
        return repository.save(user);
    }

    public List<T> getAll() {
        return repository.findAll();
    }

}
