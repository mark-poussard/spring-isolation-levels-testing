package io.poussard.mark.spring.isolationlevelstesting.domain.transaction.uniqueidx;

import io.poussard.mark.spring.isolationlevelstesting.domain.transaction.GenericUser;
import jakarta.persistence.Entity;

@Entity
public class UniqueIdxUser extends GenericUser {

    public UniqueIdxUser() {
        super();
    }

    public UniqueIdxUser(String name, String email, String password) {
        super(name, email, password);
    }
}
