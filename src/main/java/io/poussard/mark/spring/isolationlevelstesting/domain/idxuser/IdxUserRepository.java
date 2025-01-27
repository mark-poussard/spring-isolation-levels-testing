package io.poussard.mark.spring.isolationlevelstesting.domain.idxuser;

import io.poussard.mark.spring.isolationlevelstesting.domain.GenericUserRepository;
import io.poussard.mark.spring.isolationlevelstesting.domain.noidxuser.NoIdxUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IdxUserRepository extends GenericUserRepository<IdxUser> {
}
