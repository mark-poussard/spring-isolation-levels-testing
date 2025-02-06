package io.poussard.mark.spring.isolationlevelstesting.domain.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface GenericUserRepository<T extends GenericUser> extends JpaRepository<T, Long>  {
    List<T> findByName(String name);
}
