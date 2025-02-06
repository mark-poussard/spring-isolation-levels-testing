package io.poussard.mark.spring.isolationlevelstesting.domain.transaction.noidxuser;

import io.poussard.mark.spring.isolationlevelstesting.domain.transaction.GenericUser;
import jakarta.persistence.Entity;

@Entity
public class NoIdxUser extends GenericUser {

    public NoIdxUser() {
        super();
    }

    public NoIdxUser(String name, String email, String password) {
        super(name, email, password);
    }
}
