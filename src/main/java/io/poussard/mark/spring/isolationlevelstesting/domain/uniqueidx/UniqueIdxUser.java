package io.poussard.mark.spring.isolationlevelstesting.domain.uniqueidx;

import io.poussard.mark.spring.isolationlevelstesting.domain.GenericUser;
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
