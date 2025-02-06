package io.poussard.mark.spring.isolationlevelstesting.domain.transaction;

import io.poussard.mark.spring.isolationlevelstesting.domain.transaction.util.TriFunction;

@FunctionalInterface
public interface UserConstructor<T> extends TriFunction<String, String, String, T> {
}
