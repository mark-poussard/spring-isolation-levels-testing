package io.poussard.mark.spring.isolationlevelstesting.domain;

import io.poussard.mark.spring.isolationlevelstesting.infrastructure.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;

    public User create(CreateUserRequest request, Isolation isolation) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setIsolationLevel(isolation.value());
        return transactionTemplate.execute(_ -> internalCreate(request));
    }

    private User internalCreate(CreateUserRequest request) {
        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );
        if(userRepository.findByName(user.getName()).isPresent()){
            throw new IllegalArgumentException(String.format("User %s already exists.", user.getName()));
        }
        return userRepository.save(user);
    }

    @Transactional()
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
