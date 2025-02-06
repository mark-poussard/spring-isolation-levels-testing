package io.poussard.mark.spring.isolationlevelstesting.domain.transaction;

import io.poussard.mark.spring.isolationlevelstesting.domain.transaction.idxuser.IdxUser;
import io.poussard.mark.spring.isolationlevelstesting.domain.transaction.noidxuser.NoIdxUser;
import io.poussard.mark.spring.isolationlevelstesting.domain.transaction.uniqueidx.UniqueIdxUser;
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
    @Autowired
    private GenericUserRepository<UniqueIdxUser> uniqueIdxUserRepository;

    private UserService<NoIdxUser> cachedNoIdxUserService;
    private UserService<IdxUser> cachedIdxUserService;
    private UserService<UniqueIdxUser> cachedUniqueIdxUserService;

    private UserService<NoIdxUser> getNoIdxUserService(){
        if(cachedNoIdxUserService == null){
            cachedNoIdxUserService = new UserService<>(transactionManager, NoIdxUser::new, noIdxUserRepository);
        }
        return cachedNoIdxUserService;
    }

    private UserService<IdxUser> getIdxUserService(){
        if(cachedIdxUserService == null){
            cachedIdxUserService = new UserService<>(transactionManager, IdxUser::new, idxUserRepository);
        }
        return cachedIdxUserService;
    }

    private UserService<UniqueIdxUser> getUniqueIdxUserService(){
        if(cachedUniqueIdxUserService == null){
            cachedUniqueIdxUserService = new UserService<>(transactionManager, UniqueIdxUser::new, uniqueIdxUserRepository);
        }
        return cachedUniqueIdxUserService;
    }

    public UserService<?> getUserService(IndexingMode mode){
        switch (mode){
            case NO_INDEX -> {
                return getNoIdxUserService();
            }
            case INDEX -> {
                return getIdxUserService();
            }
            case UNIQUE -> {
                return getUniqueIdxUserService();
            }
            default -> throw new IllegalArgumentException("Unknown value " + mode.name());
        }
    }
}
