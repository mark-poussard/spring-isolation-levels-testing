package io.poussard.mark.spring.isolationlevelstesting.domain.idxuser;

import io.poussard.mark.spring.isolationlevelstesting.domain.GenericUser;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class IdxUser extends GenericUser {

    public IdxUser() {
        super();
    }

    public IdxUser(String name, String email, String password) {
        super(name, email, password);
    }
}
