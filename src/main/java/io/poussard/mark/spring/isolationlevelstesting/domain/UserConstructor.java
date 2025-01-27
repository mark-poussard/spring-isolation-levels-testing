package io.poussard.mark.spring.isolationlevelstesting.domain;

import io.poussard.mark.spring.isolationlevelstesting.domain.util.TriFunction;

@FunctionalInterface
public interface UserConstructor<T> extends TriFunction<String, String, String, T> {
}
