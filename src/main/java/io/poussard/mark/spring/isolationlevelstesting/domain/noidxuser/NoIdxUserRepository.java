package io.poussard.mark.spring.isolationlevelstesting.domain.noidxuser;

import io.poussard.mark.spring.isolationlevelstesting.domain.GenericUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoIdxUserRepository extends GenericUserRepository<NoIdxUser> {
}
