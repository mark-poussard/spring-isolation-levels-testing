package io.poussard.mark.spring.isolationlevelstesting.domain;

import io.poussard.mark.spring.isolationlevelstesting.domain.idxuser.IdxUser;
import io.poussard.mark.spring.isolationlevelstesting.domain.noidxuser.NoIdxUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

@Service
public class UserServiceFactory {
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private GenericUserRepository<NoIdxUser> noIdxUserRepository;
    @Autowired
    private GenericUserRepository<IdxUser> idxUserRepository;

    public UserService<?> getUserService(IndexingMode mode){
        switch (mode){
            case NO_INDEX -> {
                return new UserService<>(transactionManager, NoIdxUser::new, noIdxUserRepository);
            }
            case INDEX -> {
                return new UserService<>(transactionManager, IdxUser::new, idxUserRepository);
            }
            case UNIQUE -> throw new IllegalArgumentException("Unique index not implemented");
            default -> throw new IllegalArgumentException("Unknown value " + mode.name());
        }
    }
}
