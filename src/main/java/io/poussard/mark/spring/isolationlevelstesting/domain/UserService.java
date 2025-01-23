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

    public User createUser(CreateUserRequest request, Isolation isolation) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setIsolationLevel(isolation.value());
        return transactionTemplate.execute(_ -> internalCreateUser(request));
    }

    private User internalCreateUser(CreateUserRequest request) {
        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );
        if(userRepository.findByName(user.getName()).isPresent()){
            throw new IllegalArgumentException(String.format("User %s already exists.", user.getName()));
        }
        return userRepository.save(user); // Save the user object to the database
    }

    @Transactional()
    public List<User> getAllUsers() {
        return userRepository.findAll(); // Save the user object to the database
    }
}
