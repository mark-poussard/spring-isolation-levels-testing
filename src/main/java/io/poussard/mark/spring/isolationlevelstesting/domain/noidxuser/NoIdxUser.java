package io.poussard.mark.spring.isolationlevelstesting.domain.noidxuser;

import io.poussard.mark.spring.isolationlevelstesting.domain.GenericUser;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class NoIdxUser extends GenericUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public NoIdxUser() {
        super();
    }

    public NoIdxUser(String name, String email, String password) {
        super(name, email, password);
    }
}
