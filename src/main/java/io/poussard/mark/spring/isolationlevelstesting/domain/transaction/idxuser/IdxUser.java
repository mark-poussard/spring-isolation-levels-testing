package io.poussard.mark.spring.isolationlevelstesting.domain.transaction.idxuser;

import io.poussard.mark.spring.isolationlevelstesting.domain.transaction.GenericUser;
import jakarta.persistence.Entity;

@Entity
public class IdxUser extends GenericUser {

    public IdxUser() {
        super();
    }

    public IdxUser(String name, String email, String password) {
        super(name, email, password);
    }
}
